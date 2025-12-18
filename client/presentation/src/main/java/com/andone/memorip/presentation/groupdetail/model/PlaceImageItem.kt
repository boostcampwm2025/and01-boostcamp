package com.andone.memorip.presentation.groupdetail.model

import androidx.compose.runtime.Immutable

@Immutable
data class PlaceImageItem(
    val id: Int,
    val url: String,
    val width: Int,
    val height: Int
) {
    val aspectRatio: Float
        get() = width.toFloat() / height.toFloat()
}