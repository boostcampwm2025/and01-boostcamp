package com.andone.memorip.data.trip.model

import com.andone.memorip.domain.model.TripListItem
import kotlinx.serialization.Serializable

@Serializable
data class SimpleTripItem(
    val id: String,
    val title: String,
    val startDate: String? = null,
    val endDate: String? = null
) {
    companion object {
        fun toDomain(tripItem: SimpleTripItem): TripListItem = TripListItem(
            id = tripItem.id,
            title = tripItem.title,
            startDate = tripItem.startDate,
            endDate = tripItem.endDate
        )
    }
}