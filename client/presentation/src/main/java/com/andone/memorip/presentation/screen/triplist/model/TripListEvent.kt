package com.andone.memorip.presentation.screen.triplist.model

sealed interface TripListEvent {

    data class NavigateToTripDetail(val tripId: String) : TripListEvent

    data object NavigateToPlaceCreate : TripListEvent
}