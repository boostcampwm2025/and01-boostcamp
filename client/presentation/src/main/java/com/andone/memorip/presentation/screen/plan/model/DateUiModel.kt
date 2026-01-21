package com.andone.memorip.presentation.screen.plan.model

import java.time.LocalDate

data class DateUiModel(
    val startDay: LocalDate? = null,
    val endDay: LocalDate? = null,
    val currentDay: LocalDate? = null,
    val longClickedDay: Int? = null
) {
    val totalDays: Int
        get() = if (startDay != null && endDay != null) {
            endDay.dayOfYear - startDay.dayOfYear + 1
        } else {
            0
        }

    val selectedDay: Int
        get() = if (startDay != null && currentDay != null) {
            currentDay.dayOfYear - startDay.dayOfYear + 1
        } else {
            0
        }

    fun currentDayFromSelectedDay(selectedDay: Int): LocalDate? {
        if (startDay == null || selectedDay <= 0) return null
        return startDay.plusDays((selectedDay - 1).toLong())
    }

    fun deleteDay(dayIndex: Int): Pair<LocalDate?, LocalDate?> {
        if (startDay == null || endDay == null) return startDay to endDay
        if (dayIndex <= 0 || dayIndex > totalDays) return startDay to endDay

        return if (dayIndex == 1) {
            if (startDay.isEqual(endDay)) {
                null to null
            } else {
                val newStart = startDay.plusDays(1)
                newStart to endDay
            }
        } else {
            val newEnd = startDay.plusDays((dayIndex - 2).toLong())
            startDay to newEnd
        }
    }
}
