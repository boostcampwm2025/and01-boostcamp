package com.andone.memorip.presentation.screen.selecttrip.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

data class SelectTripUiState(
    val trips: ImmutableList<SelectTripUiModel> = emptyList<SelectTripUiModel>().toImmutableList(),
    val selectedTripIds: ImmutableSet<String> = persistentSetOf(),
    val initialSelectedTripIds: ImmutableSet<String> = persistentSetOf(),
    val isLoading: Boolean = false
)
