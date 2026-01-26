package com.andone.memorip.data.place.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceGroupsUpdateRequest(
    val addGroupIds: List<String>,
    val removeGroupIds: List<String>
)
