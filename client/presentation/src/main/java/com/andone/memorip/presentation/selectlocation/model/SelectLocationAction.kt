package com.andone.memorip.presentation.selectlocation.model

import com.naver.maps.geometry.LatLng

sealed interface SelectLocationAction {

    data class OnSelectLocationClick(val location: LatLng?) : SelectLocationAction

    data object OnBackClick : SelectLocationAction
}