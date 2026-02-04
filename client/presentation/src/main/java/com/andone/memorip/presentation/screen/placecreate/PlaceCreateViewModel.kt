package com.andone.memorip.presentation.screen.placecreate

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.model.request.Address
import com.andone.memorip.domain.model.request.PlaceCreateUpdate
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.model.TripUiModel
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateEvent
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateUiState
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import com.andone.memorip.domain.ai.ToxicityAnalyzer
import com.andone.memorip.presentation.util.splitSentences

@HiltViewModel
class PlaceCreateViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val snackBarManager: SnackBarManager,
    private val toxicityAnalyzer: ToxicityAnalyzer
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaceCreateUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PlaceCreateEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: PlaceCreateAction) {
        when (action) {
            PlaceCreateAction.OnCategoryClick -> {
                _event.trySend(PlaceCreateEvent.NavigateToCategory)
            }

            PlaceCreateAction.OnLocationClick -> {
                _event.trySend(PlaceCreateEvent.NavigateToLocation)
            }

            PlaceCreateAction.OnTripClick -> {
                _event.trySend(PlaceCreateEvent.NavigateToTrip)
            }

            is PlaceCreateAction.OnTitleChange -> {
                _uiState.update { it.copy(title = action.title) }
            }

            is PlaceCreateAction.OnContentChange -> {
                _uiState.update { it.copy(content = action.content) }
            }

            PlaceCreateAction.OnPublicChange -> {
                _uiState.update { it.copy(isPublic = !_uiState.value.isPublic) }
            }

            is PlaceCreateAction.OnImageSelect -> {
                _uiState.update { it.copy(selectedImage = action.imageUri) }
            }

            is PlaceCreateAction.OnImagesRemove -> {
                removeImage(action.imageUri)
            }

            is PlaceCreateAction.OnScrollPositionChange -> {
                _uiState.update { it.copy(scrollPosition = action.position) }
            }

            is PlaceCreateAction.OnPlaceCreate -> {
                createPlace(action.context)
            }

            is PlaceCreateAction.OnSnackBarShow -> {
                snackBarManager.show(SnackBarEvent.NETWORK_ERROR)
            }

            is PlaceCreateAction.OnTripSelect -> {
                updateTrip(action.trips)
            }

            PlaceCreateAction.OnCreateSuccess -> {
                _event.trySend(PlaceCreateEvent.NavigateToHome)
            }
        }
    }

    fun updateImages(images: List<Uri>, thumbnailImageRatio: Float) {
        _uiState.update { it.copy(images = images, thumbnailImageRatio = thumbnailImageRatio) }
    }

    fun updateLocation(location: LocationUiModel) {
        _uiState.update { it.copy(location = location) }
    }

    fun updateCategory(category: List<TagUiModel>) {
        _uiState.update { it.copy(tags = category) }
    }

    fun updateTrip(trips: List<TripUiModel>) {
        _uiState.update { it.copy(trips = trips) }
    }

    private fun removeImage(imageUri: Uri) {
        _uiState.update {
            if (it.images.size == 1) {
                snackBarManager.show(SnackBarEvent.IMAGE_COUNT_ERROR)
                return
            }

            val images = it.images - imageUri
            val selectedImage = if (it.selectedImage == imageUri) {
                images.firstOrNull()
            } else {
                it.selectedImage
            }

            it.copy(
                images = images,
                selectedImage = selectedImage
            )
        }
    }

    private fun createPlace(context: Context) {
        val uiStateValue = _uiState.value
        if (uiStateValue.images.isEmpty()
            || uiStateValue.location == null
            || uiStateValue.title.isBlank()
            || uiStateValue.trips.isEmpty()
        ) return

        viewModelScope.launch {
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

            val imageUrls = uploadImages(context, uiStateValue.images)

            placeRepository.createPlace(
                PlaceCreateUpdate(
                    tripIds = uiStateValue.trips.map { it.id },
                    title = uiStateValue.title,
                    content = uiStateValue.content,
                    tags = uiStateValue.tags.map { it.id },
                    latitude = uiStateValue.location.latitude,
                    longitude = uiStateValue.location.longitude,
                    address = Address.from(uiStateValue.location.address),
                    imageUrls = imageUrls,
                    thumbnailImageRatio = uiStateValue.thumbnailImageRatio,
                    isPublic = uiStateValue.isPublic
                )
            ).onSuccess { data ->
                onAction(PlaceCreateAction.OnCreateSuccess)
            }.onFailure { exception ->
                snackBarManager.show(SnackBarEvent.DATA_SAVE_FAILED)
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun uploadImages(context: Context, uris: List<Uri>): List<String> =
        coroutineScope {
            val deferredUploads = uris.map { uri ->
                async {
                    val file = uriToFile(context, uri)
                    if (file != null) {
                        placeRepository.uploadImage(file)
                            .map { it.imageUrl }
                            .getOrNull()
                    } else {
                        null
                    }
                }
            }
            deferredUploads.awaitAll().filterNotNull()
        }

    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("upload_image", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(tempFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
