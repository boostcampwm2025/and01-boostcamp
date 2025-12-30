package com.andone.memorip.presentation.selectlocation.model

sealed interface SelectLocationEvent {

    data class SelectLocation(val location: String) : SelectLocationEvent

    data object NavigateBack : SelectLocationEvent
}