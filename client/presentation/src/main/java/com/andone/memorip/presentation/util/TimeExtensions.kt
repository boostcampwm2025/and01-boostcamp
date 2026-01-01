package com.andone.memorip.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.andone.memorip.presentation.R
import java.time.LocalDateTime

/**
 * ex) 오후 03:00
 */
@Composable
fun LocalDateTime.toTimeString(): String {
    val amText = stringResource(R.string.time_am)
    val pmText = stringResource(R.string.time_pm)
    val period = if (hour < 12) amText else pmText
    val displayHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return stringResource(
        R.string.time_format,
        period,
        displayHour,
        minute
    )
}

/**
 * ex) 오후 03:00 - 오후 07:00
 */
@Composable
fun formatTimeRange(start: LocalDateTime, end: LocalDateTime): String {
    return stringResource(
        R.string.range_format,
        start.toTimeString(),
        end.toTimeString()
    )
}