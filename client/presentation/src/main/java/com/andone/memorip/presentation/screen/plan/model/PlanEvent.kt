package com.andone.memorip.presentation.screen.plan.model

sealed interface PlanEvent {
    data class ShowDeleteDayDialog(val day: Int?) : PlanEvent
}