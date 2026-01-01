package com.andone.memorip.presentation.selectlocation.model

import com.naver.maps.geometry.LatLng

sealed interface SelectLocationAction {

    data class OnQueryChange(val query: String) : SelectLocationAction

    data class OnSelectLocationClick(val location: LatLng?) : SelectLocationAction

    data object OnBackClick : SelectLocationAction
}