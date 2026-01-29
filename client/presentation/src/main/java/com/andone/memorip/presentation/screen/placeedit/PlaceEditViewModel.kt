package com.andone.memorip.presentation.screen.placeedit

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.presentation.model.GroupUiModel
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.screen.placeedit.model.PlaceEditAction
import com.andone.memorip.presentation.screen.placeedit.model.PlaceEditEvent
import com.andone.memorip.presentation.screen.placeedit.model.PlaceEditUiState
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@HiltViewModel(assistedFactory = PlaceEditViewModel.Factory::class)
class PlaceEditViewModel @AssistedInject constructor(
    @Assisted private val place: PlaceUiModel,
    private val placeRepository: PlaceRepository,
    private val snackBarManager: SnackBarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PlaceEditUiState(
            id = place.id,
            title = place.title,
            content = place.content,
            images = place.imageUrls.map { it.toUri() },
            location = LocationUiModel(
                name = place.locationName,
                address = place.locationName,
                roadAddress = place.locationName,
                latitude = place.latitude,
                longitude = place.longitude
            ),
            groups = place.groups.map { it.toUiModel() },
            tags = place.tags,
            isPublic = place.isPublic,
            isLoading = false
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PlaceEditEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    val initUiState = _uiState

    fun onAction(action: PlaceEditAction) {
        when (action) {
            PlaceEditAction.OnCategoryClick -> {
                _event.trySend(PlaceEditEvent.NavigateToCategory)
            }

            PlaceEditAction.OnLocationClick -> {
                _event.trySend(PlaceEditEvent.NavigateToLocation)
            }

            PlaceEditAction.OnGroupClick -> {
                _event.trySend(PlaceEditEvent.NavigateToGroup)
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

            PlaceEditAction.OnLastImageRemove -> {
                _event.trySend(PlaceEditEvent.NavigateBack)
            }

            is PlaceEditAction.OnScrollPositionChange -> {
                _uiState.update { it.copy(scrollPosition = action.position) }
            }

            PlaceEditAction.OnPlaceUpdate -> {
                updatePlace()
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

    private fun updatePlace() {

    }

    fun updateGroup(groups: List<GroupUiModel>) {
        _uiState.update { it.copy(groups = groups) }
    }

    fun updateTag(tags: List<TagUiModel>) {
        _uiState.update { it.copy(tags = tags) }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: PlaceUiModel): PlaceEditViewModel
    }
}