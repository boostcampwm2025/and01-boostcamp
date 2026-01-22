package com.andone.memorip.presentation.util

import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatters {

    val DAY_SHORT: DateTimeFormatter =
        DateTimeFormatter.ofPattern("M.d", Locale.KOREAN)

    val DATE_RANGE: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREAN)
}