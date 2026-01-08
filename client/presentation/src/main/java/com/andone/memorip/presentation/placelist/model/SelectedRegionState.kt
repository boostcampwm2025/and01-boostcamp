package com.andone.memorip.presentation.placelist.model

data class SelectedRegionState(
    val parents: List<RegionChipModel> = emptyList(),
    val children: List<RegionChipModel> = emptyList()
)

