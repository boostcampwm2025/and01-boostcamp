package com.andone.memorip.presentation.screen.selectgroup.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList

data class SelectGroupUiState(
    val groups: ImmutableList<SelectGroupUiModel> = emptyList<SelectGroupUiModel>().toImmutableList(),
    val selectedGroupIds: ImmutableSet<String> = persistentSetOf(),
    val initialSelectedGroupIds: ImmutableSet<String> = persistentSetOf(),
    val isLoading: Boolean = false
)
