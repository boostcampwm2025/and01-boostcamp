package com.andone.memorip.domain.group.dto.response

import com.andone.memorip.domain.group.entity.GroupType
import com.andone.memorip.domain.group.entity.Visibility
import com.andone.memorip.domain.place.dto.PlaceResponse
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

