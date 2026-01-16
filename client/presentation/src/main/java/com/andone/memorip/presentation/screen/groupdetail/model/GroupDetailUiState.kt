package com.andone.memorip.presentation.screen.groupdetail.model

import com.andone.memorip.presentation.model.Place

data class GroupDetailUiState(
    val groupName: String = "",
    var selectedPlace: Place? = null,
    val currentTab: Int = 0,
    val expanded: Boolean = false,
)