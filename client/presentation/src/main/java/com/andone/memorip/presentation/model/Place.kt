package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable

@Immutable
data class Place(
    val id: Int,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val thumbnailImage: ImageItem,
    val images: List<ImageItem>
)