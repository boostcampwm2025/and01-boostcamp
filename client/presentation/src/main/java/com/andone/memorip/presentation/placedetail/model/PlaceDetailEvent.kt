package com.andone.memorip.presentation.placedetail.model

sealed interface PlaceDetailEvent {
    data object NavigateBack : PlaceDetailEvent
}