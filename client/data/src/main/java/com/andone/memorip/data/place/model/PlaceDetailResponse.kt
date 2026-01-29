package com.andone.memorip.data.place.model

import com.andone.memorip.domain.model.Tag
import com.andone.memorip.domain.model.request.Address
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetailResponse(
    val placeId: String,
    val writerId: String,
    val title: String,
    val content: String?,
    val latitude: Double,
    val longitude: Double,
    val tags: List<Tag>,
    val images: List<String>,
    val groups: List<GroupCompactResponse>,
    val address: Address,
    val isMine: Boolean
)

@Serializable
data class GroupCompactResponse(
    val groupId: String,
    val groupName: String
)
