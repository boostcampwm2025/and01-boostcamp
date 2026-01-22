package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.SCROLL_DURATION
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_HOUR
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.screen.plan.utill.TimeLayoutEngine
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.toPx
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private object TimeTableConstants {
    val SCROLL_DURATION = 700
}

@Composable
fun TimeTable(
    totalMinutes: Int,
    currentDay: Int?,
    onDayScrolled: (Int) -> Unit = {},
    content: @Composable (TimeLayoutEngine, ScrollState) -> Unit
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val minuteHeightPx = MINUTE_HEIGHT_DP.dp.toPx(density)
    val engine = remember(minuteHeightPx) {
        TimeLayoutEngine(minuteHeightPx)
    }

    var isAutoScrolling by remember { mutableStateOf(false) }
    var lastDayFromScroll by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(currentDay, minuteHeightPx) {
        val day = currentDay ?: return@LaunchedEffect
        if (day <= 0) return@LaunchedEffect

        if (lastDayFromScroll == day) {
            lastDayFromScroll = null
            return@LaunchedEffect
        }

        val dayStartMinute = (day - 1) * MINUTES_PER_DAY
        val targetYPx = (dayStartMinute * minuteHeightPx).toInt()

        isAutoScrolling = true
        scrollState.animateScrollTo(
            value = targetYPx,
            animationSpec = tween(
                durationMillis = SCROLL_DURATION,
                easing = LinearOutSlowInEasing
            )
        )
        isAutoScrolling = false
    }

    LaunchedEffect(scrollState, minuteHeightPx) {
        snapshotFlow { scrollState.value }
            .map { scrollYPx ->
                val minute = scrollYPx / minuteHeightPx
                ((minute + 1) / MINUTES_PER_DAY).toInt() + 1
            }
            .distinctUntilChanged()
            .collect { day ->
                if (!isAutoScrolling && scrollState.isScrollInProgress && day > 0) {
                    lastDayFromScroll = day
                    onDayScrolled(day)
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .background(color = MemoripTheme.colors.background),
    ) {
        Box {
            HorizontalTimeGridLines(
                totalMinutes = totalMinutes,
                minuteHeightPx = minuteHeightPx
            )
            Row {
                TimeAxis(totalMinutes = totalMinutes)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = (totalMinutes * MINUTE_HEIGHT_DP).dp)
                ) {
                    content(engine,scrollState)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeTablePreview() {
    TimeTable(
        totalMinutes = MINUTES_PER_DAY,
        currentDay = 1,
        content = {_, _ -> },
    )
}

@Composable
private fun HorizontalTimeGridLines(
    totalMinutes: Int = MINUTES_PER_DAY,
    majorIntervalMinutes: Int = MINUTES_PER_HOUR,
    minuteHeightPx: Float,
) {
    val lineColor = MemoripTheme.colors.lightGray
    val strokeDp = MemoripLineWidth.TimeTick

    Canvas(modifier = Modifier.fillMaxSize()) {
        var minute = MINUTES_PER_HOUR
        while (minute <= totalMinutes) {
            val y = minute * minuteHeightPx

            drawLine(
                color = lineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeDp.toPx()
            )

            minute += majorIntervalMinutes
        }
    }
}