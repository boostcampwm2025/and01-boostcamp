package com.andone.memorip.presentation.screen.selectlocation.model

import com.andone.memorip.presentation.model.LocationUiModel

sealed interface SelectLocationEvent {

    data class SelectLocation(val location: LocationUiModel) : SelectLocationEvent

    data object NavigateBack : SelectLocationEvent
}