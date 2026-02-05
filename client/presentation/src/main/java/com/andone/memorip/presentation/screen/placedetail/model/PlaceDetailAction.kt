package com.andone.memorip.presentation.screen.placedetail.model

sealed interface PlaceDetailAction {

    data object OnBackClick : PlaceDetailAction

    data object OnAddToTripClick : PlaceDetailAction

    data object TripClick : PlaceDetailAction

    data object OnMoreClick : PlaceDetailAction

    data object OnMoreMenuDismiss : PlaceDetailAction

    data object OnEditClick : PlaceDetailAction

    data object OnDeleteClick : PlaceDetailAction

    data object OnDeleteDismiss : PlaceDetailAction

    data object OnDeleteConfirm : PlaceDetailAction

    data object OnRefreshRequested : PlaceDetailAction
}
