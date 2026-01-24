package com.andone.memorip.presentation.screen.plan.model

import android.util.Log
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

data class DateUiModel(
    val startDay: LocalDate? = null,
    val endDay: LocalDate? = null,
    val currentDay: LocalDate? = null,
    val longClickedDay: Int? = null
) {
    val totalDays: Int
        get() = if (startDay != null && endDay != null) {
            ChronoUnit.DAYS.between(startDay, endDay).toInt() + 1
        } else {
            0
        }

    val selectedDay: Int?
        get() = if (startDay != null && currentDay != null) {
            currentDay.dayOfYear - startDay.dayOfYear + 1
        } else {
            null
        }

    val totalMinutes: Int
        get() = totalDays * MINUTES_PER_DAY

    fun currentDayFromSelectedDay(selectedDay: Int): LocalDate? {
        if (startDay == null || selectedDay <= 0) return null
        return startDay.plusDays((selectedDay - 1).toLong())
    }

    fun deleteDay(dayIndex: Int): Pair<LocalDate?, LocalDate?> {
        if (startDay == null || endDay == null) return startDay to endDay
        if (dayIndex in 0 until totalDays) return startDay to endDay

        if (startDay.isEqual(endDay)) {
            return null to null
        }

        return if (dayIndex == 1) {
            val newStart = startDay.plusDays(1)
            newStart to endDay
        } else {
            startDay to endDay.minusDays(1)
        }
    }
}
