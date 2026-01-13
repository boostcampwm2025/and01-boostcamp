package com.andone.memorip.presentation.screen.groupdetail.model

import com.andone.memorip.presentation.model.Place
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class GroupDetailUiState(
    val groupName: String = "",
    val places: ImmutableList<Place> = emptyList<Place>().toImmutableList(),
    var selectedPlace: Place? = null,
    val currentTab: Int = 0,
    val expanded: Boolean = false,
)