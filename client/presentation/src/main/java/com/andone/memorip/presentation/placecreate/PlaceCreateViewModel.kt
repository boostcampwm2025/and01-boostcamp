package com.andone.memorip.presentation.placecreate

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.model.request.Address
import com.andone.memorip.domain.model.request.PlaceCreateRequest
import com.andone.memorip.domain.repository.PlaceListRepository
import com.andone.memorip.presentation.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.model.Category
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.placecreate.model.PlaceCreateEvent
import com.andone.memorip.presentation.placecreate.model.PlaceCreateUiState
import com.andone.memorip.presentation.util.SnackBarManager
import com.andone.memorip.presentation.util.SnackBarRequest
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PlaceCreateViewModel @Inject constructor(
    private val placeListRepository: PlaceListRepository,
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

            PlaceCreateAction.OnGroupClick -> {
                _event.trySend(PlaceCreateEvent.NavigateToGroup)
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

            is PlaceCreateAction.OnImagesAdd -> {
                _uiState.update { it.copy(images = it.images + action.images) }
            }

            is PlaceCreateAction.OnImagesRemove -> {
                _uiState.update { it.copy(images = it.images - action.imageUri) }
            }

            PlaceCreateAction.OnPlaceCreate -> {
                createPlace()
            }

            PlaceCreateAction.OnSnackBarShow -> {
                _event.trySend(PlaceCreateEvent.ShowSnackBar)
            }

            PlaceCreateAction.OnBackClick -> {
                _event.trySend(PlaceCreateEvent.NavigateBack)
            }
        }
    }

    fun updateCategory(category: List<Category>) {
        _uiState.update { it.copy(category = category) }
    }

    fun updateLocation(location: LocationUiModel) {
        _uiState.update { it.copy(location = location) }
    }

    fun updateGroup(group: GroupUiModel) {
        _uiState.update { it.copy(group = group) }
    }

    fun createPlace() {

    private suspend fun uploadImages(context: Context, uris: List<Uri>): List<String> =
        coroutineScope {
            val deferredUploads = uris.map { uri ->
                async {
                    val file = uriToFile(context, uri)
                    if (file != null) {
                        placeListRepository.uploadImage(file)
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