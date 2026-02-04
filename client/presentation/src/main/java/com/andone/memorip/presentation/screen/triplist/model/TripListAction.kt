package com.andone.memorip.presentation.screen.triplist.model

sealed interface TripListAction {

    data object OnFABClick : TripListAction

    data class OnTripClick(val tripId: String) : TripListAction

    data object OnCreateNewTripClick : TripListAction

    data object OnConfirmCreateTrip : TripListAction

    data class OnSearchQueryChange(val query: String) : TripListAction

    data class OnScrollStateChange(val isScrollingDown: Boolean) : TripListAction

    data object OnDismissCreateTripDialog : TripListAction

    data class OnNewTripNameChange(val name: String) : TripListAction
}