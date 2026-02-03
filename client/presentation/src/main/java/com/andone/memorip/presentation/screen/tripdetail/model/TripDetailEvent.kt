package com.andone.memorip.presentation.screen.tripdetail.model

interface TripDetailEvent {

    data object NavigateBack : TripDetailEvent

    data class NavigateToPlaceDetail(val id: String) : TripDetailEvent
}