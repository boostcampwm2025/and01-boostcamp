package com.andone.memorip.domain.place.dto

import java.time.LocalDateTime
import java.util.UUID

data class PlaceListItemResponse(
    val id: UUID,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?,
    val imageUrl: String?
)
