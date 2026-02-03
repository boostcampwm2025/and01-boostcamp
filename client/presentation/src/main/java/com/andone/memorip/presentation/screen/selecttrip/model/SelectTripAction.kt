package com.andone.memorip.presentation.screen.selecttrip.model

sealed interface SelectTripAction {

    data class OnInitialize(
        val placeId: String?,
        val initialSelectedTripIds: List<String>?
    ) : SelectTripAction

    data object OnFABClick : SelectTripAction

    data class OnTripClick(val trip: SelectTripUiModel) : SelectTripAction

    data object OnAddTripClick : SelectTripAction

    data object OnBackClick : SelectTripAction

    data object OnCheckClick : SelectTripAction

    data class OnDialogConfirmClick(val tripName: String) : SelectTripAction

    data object OnDialogCancelClick : SelectTripAction
}
