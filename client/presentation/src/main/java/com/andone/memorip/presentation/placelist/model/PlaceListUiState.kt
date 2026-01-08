package com.andone.memorip.presentation.placelist.model

data class PlaceListUiState(
    val query: String = "",
    val currentRegionList: List<RegionChipModel> = emptyList(),
    val selectedRegionState: SelectedRegionState = SelectedRegionState(),
)
