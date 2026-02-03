package com.andone.memorip.presentation.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

object DateFormatters {

    val DAY_SHORT: DateTimeFormatter =
        DateTimeFormatter.ofPattern("M.d", Locale.KOREAN)

    val DATE_RANGE: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREAN)
}

fun formatDateRange(startDate: String?, endDate: String?): String {
    if (startDate == null || endDate == null) return ""
    return try {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        "${start.format(DateFormatters.DAY_SHORT)} - ${end.format(DateFormatters.DAY_SHORT)}"
    } catch (e: Exception) {
        ""
    }
}

fun calculateDDay(targetDate: String?): String {
    if (targetDate == null) return ""

    return try {
        val target = LocalDate.parse(targetDate)
        val today = LocalDate.now()
        val daysDiff = ChronoUnit.DAYS.between(today, target).toInt()

        when {
            daysDiff > 0 -> "D-$daysDiff"
            daysDiff == 0 -> "D-Day"
            else -> "D+${-daysDiff}"
        }
    } catch (e: Exception) {
        ""
    }
}