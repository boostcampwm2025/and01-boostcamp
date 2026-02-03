package com.andone.memorip.presentation.screen.selectimage.model

import android.net.Uri

data class SelectImageUiState(
    val selectedImages: List<Uri> = emptyList(),
    val croppedImages: List<Uri> = emptyList(),
    val transformData: Map<Uri, CropTransformData> = emptyMap()
)