package com.andone.memorip.data.trip.model

import kotlinx.serialization.Serializable

@Serializable
data class TripUpdateRequest(
    val title: String,
    val visibility: String,
    val startDate: String?,
    val endDate: String?
)