package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.Group

@Immutable
data class TripUiModel(
    val id: String,
    val name: String,
    val images: List<String>,
) {
    companion object {
        fun from(trip: Group): TripUiModel {
            return TripUiModel(
                id = trip.id,
                name = trip.title,
                images = trip.images
            )
        }
    }
}
