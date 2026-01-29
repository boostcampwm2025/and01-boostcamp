package com.andone.memorip.data.trip.model

import com.andone.memorip.domain.model.Trip
import com.andone.memorip.domain.model.TripWithPlaceAdded
import com.andone.memorip.domain.model.User
import com.andone.memorip.domain.model.Visibility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TripListResponse(
    val id: String,
    val title: String,
    val visibility: GroupVisibility,
    val relatedPlaceImages: List<String>,
    val placeCount: Int,
    val createdAt: String,
    val updatedAt: String,
    val isPlaceAdded: Boolean = false,
) {
    fun toDomain(): Trip = Trip(
        id = id,
        owner = User.EMPTY,
        title = title,
        visibility = visibility.toDomain(),
        images = relatedPlaceImages,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    fun toDomainWithPlaceAdded(): TripWithPlaceAdded = TripWithPlaceAdded(
        trip = toDomain(),
        isPlaceAdded = isPlaceAdded
    )
}

@Serializable
enum class GroupVisibility {
    @SerialName("PUBLIC")
    PUBLIC,
    @SerialName("PRIVATE")
    PRIVATE;

    fun toDomain(): Visibility = when (this) {
        PUBLIC -> Visibility.PUBLIC
        PRIVATE -> Visibility.PRIVATE
    }
}