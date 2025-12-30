package com.andone.memorip.presentation.placedetail.model

sealed interface PlaceDetailAction {
    data object OnBackClick : PlaceDetailAction
}