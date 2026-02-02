package com.andone.memorip.presentation.model

import androidx.compose.runtime.Immutable
import com.andone.memorip.domain.model.Trip

@Immutable
data class TripUiModel(
    val id: String,
    val name: String,
    val images: List<String>,
    val startDate: String? = null,
    val endDate: String? = null
) {
    companion object {
        fun from(trip: Trip): TripUiModel {
            return TripUiModel(
                id = trip.id,
                name = trip.title,
                images = trip.images,
                startDate = null,
                endDate = null
            )
        }
    }
}
