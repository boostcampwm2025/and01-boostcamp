package com.andone.memorip.presentation.screen.placedetail.model

sealed interface PlaceDetailAction {

    data object OnBackClick : PlaceDetailAction

    data object OnAddToGroupClick : PlaceDetailAction
}