package com.andone.memorip.presentation.screen.plan.model

sealed interface PlanEvent {
    data class ShowDeleteDayDialog(val day: Int?) : PlanEvent
    data object ShowGroupChoiceDialog: PlanEvent
    data object ShowCalendarDialog: PlanEvent
}