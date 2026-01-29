package com.andone.memorip.data.group.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlaceTimeRequest(
    val startAt: String,
    val endAt: String
)