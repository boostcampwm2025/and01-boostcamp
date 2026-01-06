package com.andone.memorip.domain.place.dto

import com.andone.memorip.domain.group.entity.Group
import com.andone.memorip.domain.place.entity.Address
import java.util.UUID

data class PlaceDetailResponse(
    val placeId: UUID,
    val writerId: UUID,
    val title: String,
    val content: String?,
    val latitude: Double,
    val longitude: Double,
    val group: Group,
    val address: Address
)
