package com.andone.memorip.domain.model.response

import com.andone.memorip.domain.model.Tag
import com.andone.memorip.domain.model.request.Address

data class PlaceDetail(
    val placeId: String,
    val writerId: String,
    val title: String,
    val content: String?,
    val latitude: Double,
    val longitude: Double,
    val tags: List<Tag>,
    val images: List<String>,
    val groups: List<GroupCompact>,
    val address: Address,
    val isMine: Boolean,
    val isInMyGroup: Boolean,
    val isPublic: Boolean
)
