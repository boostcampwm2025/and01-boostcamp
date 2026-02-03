package com.andone.memorip.presentation.screen.tripdetail.model

import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.TripUiModel

enum class PlaceViewMode {
    GRID, LIST
}

data class TripDetailUiState(
    val currentTab: Int = 0,
    val expanded: Boolean = false,
    val mapSelectedPlace: Place? = null,
    val mapBottomSheetContent: MapBottomSheetStep = MapBottomSheetStep.PlaceList,
    val placeViewMode: PlaceViewMode = PlaceViewMode.LIST,
    val tripInfo: TripUiModel? = null
) {
    val tripName: String
        get() = tripInfo?.name ?: ""
}