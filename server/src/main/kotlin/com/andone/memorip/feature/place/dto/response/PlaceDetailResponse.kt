package com.andone.memorip.feature.place.dto.response

import com.andone.memorip.feature.place.entity.Address
import com.andone.memorip.feature.place.entity.PlaceTag
import com.fasterxml.jackson.annotation.JsonProperty
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
    val groups: List<GroupCompactResponse>,
    val address: Address,
    @get:JsonProperty("isMine")
    val isMine: Boolean
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