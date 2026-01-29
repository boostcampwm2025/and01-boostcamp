package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.PlanBlockUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PlanUiState(
    val groups: ImmutableList<GroupListUiModel> = persistentListOf(),
    val selectedGroup: GroupListUiModel? = null,
    val places: ImmutableList<Place> = persistentListOf(),
    val blocks: List<TimeBlock> = emptyList(),
    val blockUiModels: Map<String, PlanBlockUiModel> = emptyMap(),
    val date: DateUiModel = DateUiModel(),
    val isLoading: Boolean = false
)