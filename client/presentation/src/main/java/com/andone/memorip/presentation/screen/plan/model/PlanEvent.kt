package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.presentation.model.PlanBlockUiModel

sealed interface PlanEvent {
    data class ShowDeleteDayDialog(val day: Int?) : PlanEvent
    data object ShowTripChoiceDialog : PlanEvent
    data object ShowCalendarDialog : PlanEvent
    data class ShowPlaceEditDialog(val block: PlanBlockUiModel) : PlanEvent
}