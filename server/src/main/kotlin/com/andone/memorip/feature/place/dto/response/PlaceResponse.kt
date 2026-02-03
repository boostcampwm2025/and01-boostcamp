package com.andone.memorip.feature.place.dto.response

import com.andone.memorip.feature.place.entity.Address
import java.util.UUID

data class PlaceResponse(
    val placeId: UUID,
    val writerId: UUID,
    val title: String,
    val tags: List<TagResponse>,
    val thumbnailUrl: String?,
    val content: String?,
    val latitude: Double,
    val longitude: Double,
    val address: Address
)