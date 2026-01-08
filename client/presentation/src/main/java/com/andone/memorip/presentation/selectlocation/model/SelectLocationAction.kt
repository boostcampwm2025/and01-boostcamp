package com.andone.memorip.presentation.selectlocation.model

import com.andone.memorip.presentation.model.LocationUiModel
import com.naver.maps.geometry.LatLng

sealed interface SelectLocationAction {

    data class OnQueryChange(val query: String) : SelectLocationAction

    data class OnLocationClick(val location: LocationUiModel?) : SelectLocationAction

    data class OnMapClick(val location: LatLng) : SelectLocationAction

    data class OnLocationSelect(val location: LocationUiModel) : SelectLocationAction

    data object OnBackClick : SelectLocationAction
}