package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ImageItem(
    val id: Int,
    val url: String,
    val width: Int,
    val height: Int
) {
    val aspectRatio: Float
        get() = width.toFloat() / height.toFloat()

    companion object {
        fun empty(): ImageItem = ImageItem(
            id = 0,
            url = "",
            width = 1,
            height = 1
        )
    }
}