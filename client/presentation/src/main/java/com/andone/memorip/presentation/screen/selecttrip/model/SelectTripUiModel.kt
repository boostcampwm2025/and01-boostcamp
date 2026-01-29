package com.andone.memorip.presentation.screen.selecttrip.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.Group
import com.andone.memorip.domain.model.TripWithPlaceAdded
import com.andone.memorip.presentation.model.TripUiModel

@Immutable
data class SelectTripUiModel(
    val id: String,
    val name: String,
    val images: List<String>,
    val isPlaceAdded: Boolean = false
) {
    companion object {
        fun from(tripWithPlaceAdded: TripWithPlaceAdded): SelectTripUiModel {
            val trip = tripWithPlaceAdded.trip
            return SelectTripUiModel(
                id = trip.id,
                name = trip.title,
                images = trip.images,
                isPlaceAdded = tripWithPlaceAdded.isPlaceAdded
            )
        }

        fun from(trip: Group): SelectTripUiModel {
            return SelectTripUiModel(
                id = trip.id,
                name = trip.title,
                images = trip.images,
                isPlaceAdded = false
            )
        }
    }
}

fun SelectTripUiModel.toTripUiModel(): TripUiModel {
    return TripUiModel(
        id = this.id,
        name = this.name,
        images = this.images
    )
}
