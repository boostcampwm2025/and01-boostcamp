package com.andone.memorip.presentation.screen.placedetail.model

sealed interface PlaceDetailEvent {

    data object NavigateBack : PlaceDetailEvent

    data object NavigateToSelectGroup : PlaceDetailEvent

    data object NavigateToGroupList: PlaceDetailEvent
    data object PlaceAddToGroup : PlaceDetailEvent
}