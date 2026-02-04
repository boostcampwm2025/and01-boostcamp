package com.andone.memorip.feature.place.dto.response

import com.andone.memorip.feature.place.entity.Place
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class PlaceListItemResponse(
    val id: UUID,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val imageUrl: String?,
    val thumbnailImageRatio: Float,

    @get:JsonProperty("isPublic")
    val isPublic: Boolean
)

fun Place.toPlaceListItemResponse(): PlaceListItemResponse {
    return PlaceListItemResponse(
        id = id,
        title = title,
        latitude = latitude,
        longitude = longitude,
        address = address.fullAddress,
        imageUrl = thumbnailUrl,
        thumbnailImageRatio = thumbnailImageRatio,
        isPublic = isPublic
    )
}

