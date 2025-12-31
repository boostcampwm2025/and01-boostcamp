package com.andone.memorip.presentation.placelist.model

sealed interface PlaceListEvent {

    data object NavigateToPlaceCreate : PlaceListEvent

    data class NavigatePlaceDetail(val id: Int) : PlaceListEvent

    data object ShowSnackBar : PlaceListEvent
}