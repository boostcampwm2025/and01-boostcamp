package com.andone.memorip.data.trip.model

import kotlinx.serialization.Serializable

@Serializable
data class TripCreateRequest(
    val ownerId: String,
    val title: String,
    val visibility: String
)