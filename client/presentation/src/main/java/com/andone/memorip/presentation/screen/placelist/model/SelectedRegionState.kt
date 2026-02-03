package com.andone.memorip.presentation.screen.placelist.model

data class SelectedRegionState(
    val parents: List<RegionUiModel> = emptyList(),
    val child: Set<RegionUiModel> = emptySet()
)

