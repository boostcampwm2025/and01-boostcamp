package com.andone.memorip.presentation.placelist.model

data class PlaceListUiState(
    val query: String = "",
    val rootRegions: List<RegionUiModel> = emptyList(),
    val selectedRegionState: SelectedRegionState = SelectedRegionState(),
) {
    val currentRegionList: List<RegionUiModel>
        get() = when {
            selectedRegionState.parents.isEmpty() ->
                rootRegions

            selectedRegionState.parents.last().child.isNotEmpty() ->
                selectedRegionState.parents.last().child

            else ->
                selectedRegionState.parents
                    .dropLast(1)
                    .lastOrNull()
                    ?.child
                    ?: rootRegions
        }

    val currentLevel: Int
        get() = selectedRegionState.parents.size
}

