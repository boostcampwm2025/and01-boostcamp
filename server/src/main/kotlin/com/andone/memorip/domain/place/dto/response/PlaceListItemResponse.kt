package com.andone.memorip.domain.place.dto.response

import java.util.UUID

data class PlaceListItemResponse(
    val id: UUID,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val imageUrl: String?
)
