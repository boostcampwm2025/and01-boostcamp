package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import java.util.UUID

@Immutable
data class ImageItem(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
) {
    val aspectRatio: Float
        get() = width.toFloat() / height.toFloat()

    companion object {
        fun empty(): ImageItem = ImageItem(
            id = UUID.randomUUID().toString(),
            url = "",
            width = 1,
            height = 1
        )
    }
}