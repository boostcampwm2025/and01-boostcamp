package com.andone.memorip.presentation.screen.selectimage.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

data class CropTransformData(
    val scale: Float,
    val offset: Offset,
    val cropRect: Rect
)