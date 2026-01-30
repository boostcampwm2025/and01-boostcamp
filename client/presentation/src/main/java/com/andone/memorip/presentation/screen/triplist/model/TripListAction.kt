package com.andone.memorip.presentation.screen.triplist.model

sealed interface TripListAction {

    data object OnFABClick : TripListAction

    data class OnTripClick(val tripId: String) : TripListAction
}