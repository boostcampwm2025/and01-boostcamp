package com.andone.memorip.domain.place.dto.response

import com.andone.memorip.domain.group.dto.response.GroupResponse
import com.andone.memorip.domain.place.entity.Address
import com.andone.memorip.domain.place.entity.PlaceTag
import java.util.UUID

data class PlaceDetailResponse(
    val placeId: UUID,
    val writerId: UUID,
    val title: String,
    val tags: List<TagResponse>,
    val images: List<String>,
    val content: String?,
    val latitude: Double,
    val longitude: Double,
    val group: GroupResponse,
    val address: Address
)

data class TagResponse(
    val id: UUID,
    val color: String,
    val name: String
)

fun PlaceTag.toTagResponse(): TagResponse = TagResponse(
    id = this.tag.id,
    color = this.tag.colorHex,
    name = this.tag.name
)