package com.andone.memorip.data.trip.model

import com.andone.memorip.domain.model.TripPlace
import kotlinx.serialization.Serializable
import kotlin.String

@Serializable
data class TripPlaceItem(
    val groupPlaceId: String,
    val placeId: String,
    val title: String,
    val thumbnailUrl: String? = null,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val startAt: String? = null,
    val endAt: String? = null
) {
    companion object {
        fun toDomain(tripPlace: TripPlaceItem): TripPlace = TripPlace(
            tripPlaceId = tripPlace.groupPlaceId,
            placeId = tripPlace.placeId,
            title = tripPlace.title,
            thumbnail = tripPlace.thumbnailUrl,
            address = tripPlace.address,
            latitude = tripPlace.latitude,
            longitude = tripPlace.longitude,
            startAt = tripPlace.startAt,
            endAt = tripPlace.endAt
        )
    }
}