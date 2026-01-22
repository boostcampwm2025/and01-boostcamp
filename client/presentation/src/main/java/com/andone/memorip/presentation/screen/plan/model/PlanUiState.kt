package com.andone.memorip.presentation.screen.plan.model

data class PlanUiState(
    val blocks: List<TimeBlock>,
    val date: DateUiModel = DateUiModel()
)