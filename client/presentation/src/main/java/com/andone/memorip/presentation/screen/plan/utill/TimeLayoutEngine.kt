package com.andone.memorip.presentation.screen.plan.utill

import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.PlanBlockUiModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class TimeLayoutEngine(private val minuteHeightPx: Float) {
    fun blockStartYPx(block: TimeBlock): Float {
        val dayStartMinute = (block.day - 1) * MINUTES_PER_DAY
        return ((dayStartMinute + block.startMinute) * minuteHeightPx)
    }

    fun blockStartYPx(startDate: LocalDate, block: PlanBlockUiModel): Float {
        val diff = ChronoUnit.MINUTES.between(startDate.atStartOfDay(), block.startDateTime).toInt()
        return diff * minuteHeightPx
    }
    fun yPxToStartMinute(yPx: Float): Int = (yPx / minuteHeightPx).toInt()
}

