package com.andone.memorip.presentation.screen.plan.utill

import com.andone.memorip.presentation.screen.plan.model.TimeBlock
import kotlin.text.toInt

class TimeLayoutEngine(private val minuteHeightPx: Float) {
    fun blockStartYPx(block: TimeBlock): Float {
        val dayStartMinute = (block.day - 1) * MINUTES_PER_DAY
        return ((dayStartMinute + block.startMinute) * minuteHeightPx)
    }
    fun yPxToStartMinute(yPx: Float): Int = (yPx / minuteHeightPx).toInt()
}

