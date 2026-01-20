package com.andone.memorip.presentation.screen.plan.model

data class PlanUiState(
    val blocks: List<TimeBlock>,
    val totalDays: Int = 0,
    val selectedDay: Int = 0,
    val longClickedDay: Int? = null
)