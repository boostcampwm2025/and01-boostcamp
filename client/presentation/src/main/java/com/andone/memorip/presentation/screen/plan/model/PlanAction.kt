package com.andone.memorip.presentation.screen.plan.model

import com.andone.memorip.presentation.model.Place
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface PlanAction {
    data class BlockMoved(val id: String, val newStartMinute: Int) : PlanAction
    data class BlockClick(val id: String) : PlanAction
    data class BlockSlide(val id: String) : PlanAction
    data class BottomBlockDragEnd(val item: Place, val startMinute: Int) : PlanAction
    object AddDayClick : PlanAction
    object RemoveDayClick : PlanAction
    object RemoveDayCancelClick : PlanAction
    data class RemoveDayDialogConfirmClick(val day: Int) : PlanAction
    data class DayLongClick(val day: Int) : PlanAction
    data class DayClick(val day: Int) : PlanAction
    data class SelectDateDialogConfirmClick(val start: LocalDate, val end: LocalDate) : PlanAction
    data class DayScrolled(val day: Int) : PlanAction
    data object TripChoiceClick : PlanAction
    data class TripChoiceDialogConfirmClick(val selectedTrip: TripListUiModel) : PlanAction
    data object ShowCalendarClick : PlanAction
    data class PlanEditDialogConfirmClick(val id: String, val startDateTime: LocalDateTime, val endDateTime: LocalDateTime) : PlanAction
    data object PlanEditCancelClick : PlanAction
}