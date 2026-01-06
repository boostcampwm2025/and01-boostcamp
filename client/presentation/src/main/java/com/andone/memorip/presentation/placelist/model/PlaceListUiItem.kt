package com.andone.memorip.presentation.placelist.model

import com.andone.memorip.domain.model.PlaceListItem
import com.andone.memorip.presentation.model.ImageItem

data class PlaceListUiItem(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val thumbnailImage: ImageItem,
)

fun PlaceListItem.toUiModel(): PlaceListUiItem =
    PlaceListUiItem(
        id = id,
        name = title,
        latitude = latitude,
        longitude = longitude,
        address = address,
        thumbnailImage = ImageItem(
            id = 0,
            url = imageUrl,
            width = 1,
            height = 1
        ),
    )
