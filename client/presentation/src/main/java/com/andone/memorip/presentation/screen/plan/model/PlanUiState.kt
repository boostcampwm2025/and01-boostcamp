package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.model.GroupUiModel
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.PlanBlockUiModel
import kotlinx.collections.immutable.ImmutableList

data class PlanUiState(
    val groups: ImmutableList<GroupUiModel>,
    val selectedGroup: GroupUiModel,
    val places: ImmutableList<Place>,
    val blocks: List<TimeBlock>,
    val blockUiModels: Map<String, PlanBlockUiModel> = emptyMap(),
    val date: DateUiModel = DateUiModel()
)