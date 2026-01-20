package com.andone.memorip.presentation.screen.plan.utill

import com.andone.memorip.presentation.screen.plan.model.TimeBlock

const val MINUTE_HEIGHT_DP = 1.2f
const val MINUTES_PER_DAY = 24 * 60
class TimeLayoutEngine(
    private val minuteHeightPx: Float
) {
    fun blockStartYPx(block: TimeBlock): Float = block.startMinute * minuteHeightPx
    fun yPxToStartMinute(yPx: Float): Int = (yPx / minuteHeightPx).toInt()
}