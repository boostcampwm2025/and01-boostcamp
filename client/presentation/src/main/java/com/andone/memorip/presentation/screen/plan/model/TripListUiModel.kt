package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.domain.model.TripListItem
import java.time.LocalDate

data class TripListUiModel(
    val id: String,
    val title: String,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
) {
    companion object {
        fun from(trip: TripListItem): TripListUiModel = TripListUiModel(
            id = trip.id,
            title = trip.title,
            startDate = if (trip.startDate != null) LocalDate.parse(trip.startDate) else null,
            endDate = if (trip.endDate != null) LocalDate.parse(trip.endDate) else null
        )
    }
}
