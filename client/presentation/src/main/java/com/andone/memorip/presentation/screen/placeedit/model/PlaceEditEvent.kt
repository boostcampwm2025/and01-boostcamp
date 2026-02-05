package com.andone.memorip.presentation.screen.placeedit.model

sealed interface PlaceEditEvent {

    data object NavigateBack : PlaceEditEvent

    data object NavigateBackAfterUpdate : PlaceEditEvent

    data object NavigateToCategory : PlaceEditEvent

    data object NavigateToLocation : PlaceEditEvent

    data object NavigateToTrip : PlaceEditEvent
}