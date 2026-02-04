package com.andone.memorip.presentation.screen.plan.utill

import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.model.PlanBlockUiModel
import com.andone.memorip.presentation.util.getMinuteDiff
import java.time.LocalDate

class TimeLayoutEngine(private val minuteHeightPx: Float) {
    fun blockStartYPx(block: TimeBlock): Float {
        val dayStartMinute = (block.day - 1) * MINUTES_PER_DAY
        return ((dayStartMinute + block.startMinute) * minuteHeightPx)
    }

    fun blockStartYPx(startDate: LocalDate, block: PlanBlockUiModel): Float {
        return getMinuteDiff(startDate.atStartOfDay(), block.startDateTime) * minuteHeightPx
    }

    fun yPxToStartMinute(yPx: Float): Int = (yPx / minuteHeightPx).toInt()
}

