package com.andone.memorip.data.trip.model

import kotlinx.serialization.Serializable

@Serializable
data class AddPlaceToTripRequest(
    val placeId: String
)