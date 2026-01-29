package com.andone.memorip.data.trip.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlaceTimeRequest(
    val startAt: String,
    val endAt: String
)