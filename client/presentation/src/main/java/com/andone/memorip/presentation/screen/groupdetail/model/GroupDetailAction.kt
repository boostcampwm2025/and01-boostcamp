package com.andone.memorip.presentation.screen.groupdetail.model

import com.andone.memorip.presentation.model.Place
import com.naver.maps.map.Projection

sealed interface GroupDetailAction {

    data class OnPlaceClick(val id: String) : GroupDetailAction

    data class OnTabClick(val currentTab: Int) : GroupDetailAction

    data object OnMenuClick : GroupDetailAction

    data object OnSearchClick : GroupDetailAction

    data object OnBackClick : GroupDetailAction

    data class OnMapPlaceClick(val place: Place) : GroupDetailAction

    data object OnMapPlaceClose : GroupDetailAction

    // Map 관련 액션
    data class OnMapPlacesUpdate(val places: List<Place>) : GroupDetailAction

    data object OnMapInitialized : GroupDetailAction

    data class OnMapCameraChange(val projection: Projection?, val zoom: Double) : GroupDetailAction
}
