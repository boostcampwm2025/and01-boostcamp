package com.andone.memorip.presentation.screen.selecttrip.model

sealed interface SelectTripEvent {

    data object NavigateBack : SelectTripEvent

    data class SelectTrip(val trips: List<SelectTripUiModel>) : SelectTripEvent

    data object ShowDialog : SelectTripEvent

    data object DismissDialog : SelectTripEvent

    data object ShowSnackBar : SelectTripEvent

    data object PlaceTripsUpdated : SelectTripEvent
}
