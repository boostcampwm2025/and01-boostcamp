package com.andone.memorip.presentation.screen.groupdetail.model

import com.andone.memorip.presentation.model.Place

data class GroupDetailUiState(
    val groupName: String = "",
    val currentTab: Int = 0,
    val expanded: Boolean = false,
    val mapSelectedPlace: Place? = null,
    val mapBottomSheetContent: MapBottomSheetStep = MapBottomSheetStep.PlaceList,
)