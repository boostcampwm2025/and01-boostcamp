package com.andone.memorip.presentation.screen.placedetail.model

sealed interface PlaceDetailEvent {

    data object NavigateBack : PlaceDetailEvent

    data object NavigateToSelectGroup : PlaceDetailEvent

    data object NavigateToGroupList : PlaceDetailEvent

    data object NavigateToPlaceEdit : PlaceDetailEvent

    data object ShowMoreMenu : PlaceDetailEvent

    data object HideMoreMenu : PlaceDetailEvent

    data object ShowDeleteDialog : PlaceDetailEvent

    data object HideDeleteDialog : PlaceDetailEvent
}
