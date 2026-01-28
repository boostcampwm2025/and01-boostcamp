package com.andone.memorip.feature.place.dto.response

import java.time.LocalDateTime
import java.util.UUID

data class GroupPlaceListResponse(
    val groupPlaceId: UUID,
    val placeId: UUID,
    val title: String,
    val thumbnailUrl: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?
)