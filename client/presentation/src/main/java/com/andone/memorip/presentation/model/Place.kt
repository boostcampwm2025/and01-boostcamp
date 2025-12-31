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
){
    companion object {
        fun empty(): Place = Place(
            id = 0,
            name = "",
            latitude = 0.0,
            longitude = 0.0,
            thumbnailImage = ImageItem.empty(),
            images = emptyList()
        )
    }
}