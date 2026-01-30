package com.andone.memorip.presentation.screen.placedetail.model

sealed interface PlaceDetailEvent {

    data object NavigateBack : PlaceDetailEvent

    data object NavigateToSelectTrip : PlaceDetailEvent

    data object NavigateToTripList : PlaceDetailEvent

    data object PlaceAddToTrip : PlaceDetailEvent

    data object ShowMoreMenu : PlaceDetailEvent

    data object HideMoreMenu : PlaceDetailEvent

    data object ShowDeleteDialog : PlaceDetailEvent

    data object HideDeleteDialog : PlaceDetailEvent
}
