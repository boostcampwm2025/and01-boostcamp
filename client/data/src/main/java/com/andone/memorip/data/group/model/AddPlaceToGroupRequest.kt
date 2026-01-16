package com.andone.memorip.data.group.model

import kotlinx.serialization.Serializable

@Serializable
data class AddPlaceToGroupRequest(
    val placeId: String
)