package com.andone.memorip.presentation.placecreate.model

sealed interface PlaceCreateEvent {

    data object NavigateBack: PlaceCreateEvent
}