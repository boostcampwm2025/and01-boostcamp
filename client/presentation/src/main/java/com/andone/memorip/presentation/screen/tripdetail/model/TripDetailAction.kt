package com.andone.memorip.presentation.screen.tripdetail.model

import com.andone.memorip.presentation.model.Place
import com.naver.maps.map.Projection

sealed interface TripDetailAction {

    data object OnMenuClick : TripDetailAction

    data object OnSearchClick : TripDetailAction

    data object OnBackClick : TripDetailAction

    data class OnPlaceClick(val place: Place) : TripDetailAction

    data object OnPlaceDetailBottomSheetClose : TripDetailAction

    data object OnViewModeToggle : TripDetailAction

    // Map 관련 액션
    data class OnMapPlacesUpdate(val places: List<Place>) : TripDetailAction

    data object OnMapInitialized : TripDetailAction

    data class OnMapCameraChange(val projection: Projection?, val zoom: Double) : TripDetailAction
}
