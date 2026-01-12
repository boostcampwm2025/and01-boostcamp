package com.andone.memorip.presentation.screen.placedetail.model

sealed interface PlaceDetailEvent {
    data object NavigateBack : PlaceDetailEvent
}