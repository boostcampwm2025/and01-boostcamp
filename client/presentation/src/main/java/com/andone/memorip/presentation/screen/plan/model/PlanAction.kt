package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.presentation.model.GroupUiModel
import com.andone.memorip.presentation.model.Place
import java.time.LocalDate

sealed interface PlanAction {
    data class BlockMoved(val id: String, val newStartMinute: Int) : PlanAction

    data class ItemDragEnd(val item: Place, val startMinute: Int): PlanAction

    object AddDay : PlanAction

    object RemoveDayClick : PlanAction

    object RemoveCancel : PlanAction

    data class RemoveDay(val day: Int) : PlanAction

    data class LongClick(val day: Int) : PlanAction

    data class SelectDay(val day: Int) : PlanAction

    data class DateSelected(val start: LocalDate, val end: LocalDate) : PlanAction

    data class DayScrolled(val day: Int) : PlanAction
    data object GroupChoiceClick : PlanAction
    data class GroupChoiceConfirmClick(val selectedGroup: GroupUiModel) : PlanAction
    data object ShowCalendarClick: PlanAction
}
