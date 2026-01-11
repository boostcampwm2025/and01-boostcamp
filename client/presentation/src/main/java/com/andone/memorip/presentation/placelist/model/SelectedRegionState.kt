package com.andone.memorip.presentation.placelist.model

data class SelectedRegionState(
    val parents: List<RegionUiModel> = emptyList(),
    val children: List<RegionUiModel> = emptyList()
)

