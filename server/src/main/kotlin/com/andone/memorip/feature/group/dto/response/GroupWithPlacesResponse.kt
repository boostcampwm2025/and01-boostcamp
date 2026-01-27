package com.andone.memorip.feature.group.dto.response

import com.andone.memorip.feature.group.entity.GroupType
import com.andone.memorip.feature.group.entity.Visibility
import com.andone.memorip.feature.place.dto.response.PlaceResponse
import java.time.LocalDateTime
import java.util.UUID

data class GroupWithPlacesResponse(
    val id: UUID,
    val title: String,
    val visibility: Visibility,
    val type: GroupType,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val places: List<PlaceResponse>
)

