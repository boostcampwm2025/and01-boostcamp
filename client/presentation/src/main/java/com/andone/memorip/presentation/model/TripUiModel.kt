package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.Trip
import com.andone.memorip.presentation.screen.placedetail.model.TripCompactUiModel

@Immutable
data class TripUiModel(
    val id: String,
    val name: String,
    val images: List<String>,
) {
    companion object {
        fun from(trip: Trip): TripUiModel {
            return TripUiModel(
                id = trip.id,
                name = trip.title,
                images = trip.images
            )
        }
    }
}

fun TripCompactUiModel.toUiModel(): TripUiModel {
    return TripUiModel(
        id = tripId,
        name = tripName,
        images = emptyList()
    )
}