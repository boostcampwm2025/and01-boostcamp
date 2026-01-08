package com.andone.memorip.presentation.placedetail.model

sealed interface PlaceDetailEvent {
    data object NavigateBack : PlaceDetailEvent
    data class ShowError(val error: Throwable): PlaceDetailEvent
    data class ShowMessage(val message: String): PlaceDetailEvent
}