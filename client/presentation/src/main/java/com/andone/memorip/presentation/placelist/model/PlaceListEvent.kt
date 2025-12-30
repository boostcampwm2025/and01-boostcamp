package com.andone.memorip.presentation.placelist.model

sealed interface PlaceListEvent {

    data class NavigateToGroupDetail(val groupId: String) : PlaceListEvent

    data object NavigateToPlaceCreate : PlaceListEvent

    data object ShowSnackBar : PlaceListEvent
}