package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.model.PlanBlockUiModel

data class PlanUiState(
    val blocks: List<TimeBlock>,
    val blockUiModels: Map<String, PlanBlockUiModel> = emptyMap(),
    val date: DateUiModel = DateUiModel()
)