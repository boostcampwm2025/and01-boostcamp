package com.andone.memorip.presentation.screen.selecttrip.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

data class SelectTripUiState(
    val trips: ImmutableList<SelectTripUiModel> = emptyList<SelectTripUiModel>().toImmutableList(),
    val selectedTripIds: ImmutableSet<String> = persistentSetOf(),
    val initialSelectedTripIds: ImmutableSet<String> = persistentSetOf(),
    val isPlaceMine: Boolean = true,
    val isLoading: Boolean = false
) {
    val canSave: Boolean
        get() {
            val hasChanges = selectedTripIds != initialSelectedTripIds
            return if (isPlaceMine) {
                hasChanges && selectedTripIds.isNotEmpty()
            } else {
                hasChanges
            }
        }
}
