package com.andone.memorip.presentation.screen.plan.model

import java.time.LocalDate

sealed interface PlanAction {
    data class BlockMoved(val id: String, val newStartMinute: Int) : PlanAction

    object AddDay : PlanAction
    object RemoveDayClick : PlanAction

    object RemoveCancel : PlanAction

    data class RemoveDay(val day: Int) : PlanAction

    data class LongClick(val day: Int) : PlanAction

    data class SelectDay(val day: Int) : PlanAction

    data class DateSelected(val start: LocalDate, val end: LocalDate) : PlanAction

    data class DayScrolled(val day: Int) : PlanAction
}
