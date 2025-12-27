package com.andone.memorip.presentation.selectlocation.model

sealed interface SelectLocationAction {

    data class OnSelectLocationClick(val location: String) : SelectLocationAction

    data object OnBackClick : SelectLocationAction
}