package com.andone.memorip.presentation.placecreate.model

sealed interface PlaceCreateEvent {

    data object NavigateBack : PlaceCreateEvent

    data object NavigateToCategory : PlaceCreateEvent

    data object NavigateToLocation : PlaceCreateEvent

    data object NavigateToGroup : PlaceCreateEvent
}