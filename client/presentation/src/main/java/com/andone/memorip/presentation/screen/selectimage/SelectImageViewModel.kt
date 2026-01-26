package com.andone.memorip.presentation.screen.selectimage

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.screen.selectimage.model.CropTransformData
import com.andone.memorip.presentation.screen.selectimage.model.SelectImageAction
import com.andone.memorip.presentation.screen.selectimage.model.SelectImageEvent
import com.andone.memorip.presentation.screen.selectimage.model.SelectImageUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SelectImageViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SelectImageUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SelectImageEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: SelectImageAction) {
        when (action) {
            is SelectImageAction.OnImagesSelect -> {
                selectImages(action.images)
            }

            is SelectImageAction.OnImagesCrop -> {
                addImages(action.images, action.transformData)
            }

            SelectImageAction.OnBack -> {
                _event.trySend(SelectImageEvent.NavigateBack)
            }
        }
    }

    private fun selectImages(images: List<Uri>) {
        _uiState.update { it.copy(selectedImages = images) }
    }

    private fun addImages(images: List<Uri>, transformData: Map<Uri, CropTransformData>) {
        _uiState.update { it.copy(croppedImages = images, transformData = transformData) }
        _event.trySend(SelectImageEvent.NavigateToSelectLocation(images))
    }
}