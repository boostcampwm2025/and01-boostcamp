package com.andone.memorip.presentation.screen.placelist.model

import com.andone.memorip.presentation.model.TagUiModel

data class PlaceListUiState(
    val query: String = "",
    val rootRegions: List<RegionUiModel> = emptyList(),
    val selectedRegionState: SelectedRegionState = SelectedRegionState(),
    val tags: List<TagUiModel> = emptyList(),
    val selectedTags: Set<TagUiModel> = emptySet()
) {
    val currentRegionList: List<RegionUiModel>
        get() {
            val baseList = when {
                selectedRegionState.parents.isEmpty() ->
                    rootRegions

                selectedRegionState.parents.last().child.isNotEmpty() ->
                    selectedRegionState.parents.last().child

                else ->
                    selectedRegionState.parents
                        .dropLast(n = 1)
                        .lastOrNull()
                        ?.child
                        ?: rootRegions
            }

            val selectedIds = selectedRegionState.selectedIds()

            return baseList.map { region ->
                region.copy(isSelected = region.id in selectedIds)
            }
        }

    val currentLevel: Int
        get() = if (selectedRegionState.child.isNotEmpty()) {
            selectedRegionState.parents.size + 1
        } else {
            selectedRegionState.parents.size
        }

    private fun SelectedRegionState.selectedIds(): Set<String> =
        parents.map { it.id }.toSet() +
                child.map { it.id }.toSet()

}