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
}
