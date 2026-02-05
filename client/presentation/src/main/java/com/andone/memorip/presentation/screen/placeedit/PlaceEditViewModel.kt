package com.andone.memorip.presentation.screen.placeedit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.ai.ToxicityAnalyzer
import com.andone.memorip.domain.model.request.Address
import com.andone.memorip.domain.model.request.PlaceCreateUpdate
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.model.TripUiModel
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.screen.placeedit.model.PlaceEditAction
import com.andone.memorip.presentation.screen.placeedit.model.PlaceEditEvent
import com.andone.memorip.presentation.screen.placeedit.model.toUiState
import com.andone.memorip.presentation.util.BitmapCropUtil.getAspectRatioFromUrl
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import com.andone.memorip.presentation.util.splitSentences
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

@HiltViewModel(assistedFactory = PlaceEditViewModel.Factory::class)
class PlaceEditViewModel @AssistedInject constructor(
    @Assisted private val place: PlaceUiModel,
    private val placeRepository: PlaceRepository,
    private val snackBarManager: SnackBarManager,
    private val toxicityAnalyzer: ToxicityAnalyzer
) : ViewModel() {

    private val _uiState = MutableStateFlow(place.toUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PlaceEditEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    private val updatePlaceMutex = Mutex()

    val isUpdateEnabled = _uiState.map {
        val isValueRequired = it.images.isNotEmpty() &&
                it.location != null &&
                it.title.isNotBlank() &&
                it.trips.isNotEmpty()

        val isChanged = it.copy(selectedImage = null, scrollPosition = 0, isLoading = false) !=
                place.toUiState().copy(selectedImage = null, scrollPosition = 0, isLoading = false)

        isValueRequired && isChanged
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun onAction(action: PlaceEditAction) {
        when (action) {
            PlaceEditAction.OnCategoryClick -> {
                _event.trySend(PlaceEditEvent.NavigateToCategory)
            }

            PlaceEditAction.OnLocationClick -> {
                _event.trySend(PlaceEditEvent.NavigateToLocation)
            }

            PlaceEditAction.OnTripClick -> {
                _event.trySend(PlaceEditEvent.NavigateToTrip)
            }

            is PlaceEditAction.OnTitleChange -> {
                _uiState.update { it.copy(title = action.title) }
            }

            is PlaceEditAction.OnContentChange -> {
                _uiState.update { it.copy(content = action.content) }
            }

            PlaceEditAction.OnPublicChange -> {
                _uiState.update { it.copy(isPublic = !_uiState.value.isPublic) }
            }

            is PlaceEditAction.OnImageSelect -> {
                _uiState.update { it.copy(selectedImage = action.imageUri) }
            }

            is PlaceEditAction.OnImagesRemove -> {
                removeImage(action.imageUri)
            }

            is PlaceEditAction.OnScrollPositionChange -> {
                _uiState.update { it.copy(scrollPosition = action.position) }
            }

            is PlaceEditAction.OnPlaceUpdate -> {
                updatePlace(action.context)
            }

            is PlaceEditAction.OnSnackBarShow -> {

            }

            PlaceEditAction.OnBackClick -> {
                _event.trySend(PlaceEditEvent.NavigateBack)
            }
        }
    }

    private fun removeImage(imageUri: Uri) {
        _uiState.update {
            if (it.images.size == 1) {
                snackBarManager.show(SnackBarEvent.IMAGE_COUNT_ERROR)
                return
            }

            val imageUrls = it.images - imageUri
            val selectedImage = if (it.selectedImage == imageUri) {
                imageUrls.firstOrNull()
            } else {
                it.selectedImage
            }

            it.copy(
                images = imageUrls,
                selectedImage = selectedImage
            )
        }
    }

    private fun updatePlace(context: Context) {
        val uiStateValue = _uiState.value
        if (uiStateValue.images.isEmpty()
            || uiStateValue.location == null
            || uiStateValue.title.isBlank()
            || uiStateValue.trips.isEmpty()
        ) return

        if (!updatePlaceMutex.tryLock()) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val sentences =
                    splitSentences(_uiState.value.title) +
                            splitSentences(_uiState.value.content)

                for (sentence in sentences) {
                    val result = toxicityAnalyzer.predict(sentence)
                    val label = result.firstOrNull()?.first

                    if (label != null) {
                        _uiState.update {
                            it.copy(
                                contentErrorLabel = label,
                                isLoading = false
                            )
                        }
                        return@launch
                    }
                }

                placeRepository.updatePlace(
                    placeId = uiStateValue.id,
                    place = PlaceCreateUpdate(
                        tripIds = uiStateValue.trips.map { it.id },
                        title = uiStateValue.title,
                        content = uiStateValue.content,
                        tags = uiStateValue.tags.map { it.id },
                        latitude = uiStateValue.location.latitude,
                        longitude = uiStateValue.location.longitude,
                        address = Address.from(uiStateValue.location.address),
                        imageUrls = uiStateValue.images.map { it.toString() },
                        thumbnailImageRatio = getAspectRatioFromUrl(
                            context = context,
                            imageUrl = uiStateValue.images.first().toString()
                        ),
                        isPublic = uiStateValue.isPublic
                    )
                ).onSuccess {
                    _event.trySend(PlaceEditEvent.NavigateBackAfterUpdate)
                }.onFailure {
                    snackBarManager.show(SnackBarEvent.NETWORK_ERROR)
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
                updatePlaceMutex.unlock()
            }
        }
    }

    fun updateTrip(trips: List<TripUiModel>) {
        _uiState.update { it.copy(trips = trips) }
    }

    fun updateTag(tags: List<TagUiModel>) {
        _uiState.update { it.copy(tags = tags) }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: PlaceUiModel): PlaceEditViewModel
    }
}