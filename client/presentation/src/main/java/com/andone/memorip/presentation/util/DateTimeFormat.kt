package com.andone.memorip.presentation.util

import android.content.res.Resources
import com.andone.memorip.presentation.R
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

fun formatDateRange(resources: Resources, startDate: String?, endDate: String?): String {
    if (startDate == null || endDate == null) return ""
    return try {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        resources.getString(
            R.string.range_format,
            start.format(DateFormatters.DAY_SHORT),
            end.format(DateFormatters.DAY_SHORT)
        )
    } catch (e: Exception) {
        ""
    }
}

fun calculateDDay(resources: Resources, targetDate: String?): String {
    if (targetDate == null) return ""

    return try {
        val target = LocalDate.parse(targetDate)
        val today = LocalDate.now()
        val daysDiff = ChronoUnit.DAYS.between(today, target).toInt()

        when {
            daysDiff > 0 -> resources.getString(R.string.d_day_minus_format, daysDiff)
            daysDiff == 0 -> resources.getString(R.string.d_day)
            else -> resources.getString(R.string.d_day_plus_format, -daysDiff)
        }
    } catch (e: Exception) {
        ""
    }
}