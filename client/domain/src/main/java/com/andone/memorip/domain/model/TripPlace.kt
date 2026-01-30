package com.andone.memorip.domain.model

data class TripPlace(
    val tripPlaceId: String,
    val placeId: String,
    val title: String,
    val thumbnail: String? = null,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val startAt: String?,
    val endAt: String?
)