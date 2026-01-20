package com.andone.memorip.presentation.screen.plan.utill

import com.andone.memorip.presentation.screen.plan.model.TimeBlock

const val MINUTE_HEIGHT_DP = 2f
const val MINUTES_PER_DAY = 24 * 60
class TimeLayoutEngine(
    private val minuteHeightPx: Float
) {
    fun blockStartYPx(block: TimeBlock): Float =
        minuteToYPx(block.startMinute, minuteHeightPx)

    fun yPxToStartMinute(yPx: Float): Int =
        yPxToMinute(yPx, minuteHeightPx)
}

fun minuteToYPx(
    minute: Int,
    minuteHeightPx: Float
): Float = minute * minuteHeightPx

fun yPxToMinute(
    yPx: Float,
    minuteHeightPx: Float
): Int = (yPx / minuteHeightPx).toInt()