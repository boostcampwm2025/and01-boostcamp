package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class ImageItem(
    val id: Int,
    val url: String,
    val aspectRatio: Float = 1f
) {

    companion object {
        fun empty(): ImageItem = ImageItem(
            id = 0,
            url = "",
        )
    }
}