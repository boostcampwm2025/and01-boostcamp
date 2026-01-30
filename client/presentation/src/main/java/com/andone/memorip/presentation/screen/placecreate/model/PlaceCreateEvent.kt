package com.andone.memorip.presentation.screen.placecreate.model

sealed interface PlaceCreateEvent {

    data object NavigateToHome : PlaceCreateEvent

    data object NavigateToCategory : PlaceCreateEvent

    data object NavigateToLocation : PlaceCreateEvent

    data object NavigateToTrip : PlaceCreateEvent
}