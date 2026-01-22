package com.andone.memorip.presentation.screen.plan.utill

import com.andone.memorip.presentation.screen.plan.model.TimeBlock
class TimeLayoutEngine(
    private val minuteHeightPx: Float
) {
    fun blockStartYPx(block: TimeBlock): Float = block.startMinute * minuteHeightPx
    fun yPxToStartMinute(yPx: Float): Int = (yPx / minuteHeightPx).toInt()
}