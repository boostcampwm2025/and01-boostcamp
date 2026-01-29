package com.andone.memorip.presentation.screen.tripdetail.model

import com.andone.memorip.presentation.model.Place

data class TripDetailUiState(
    val tripName: String = "",
    val currentTab: Int = 0,
    val expanded: Boolean = false,
    val mapSelectedPlace: Place? = null,
    val mapBottomSheetContent: MapBottomSheetStep = MapBottomSheetStep.PlaceList,
)