package com.andone.memorip.presentation.screen.plan.model

data class PlanUiState(
    val blocks: List<TimeBlock>,
    val totalDays: Int = 1,
    val selectedDay: Int = 1,
    val longClickedDay: Int? = null
)