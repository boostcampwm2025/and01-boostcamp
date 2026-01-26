package com.andone.memorip.data.place.model

import com.andone.memorip.domain.model.PlaceListItem
import kotlinx.serialization.Serializable

@Serializable
data class PlaceListItemResponse(
    val id: String,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val imageUrl: String? = null,
    val isPublic: Boolean
)

fun PlaceListItemResponse.toDomain(): PlaceListItem =
    PlaceListItem(
        id = id,
        title = title,
        latitude = latitude,
        longitude = longitude,
        address = address,
        imageUrl = imageUrl ?: "",
        isPublic = isPublic
    )

