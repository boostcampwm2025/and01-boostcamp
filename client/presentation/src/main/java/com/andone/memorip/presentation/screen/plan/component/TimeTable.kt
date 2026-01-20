package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.screen.plan.utill.TimeLayoutEngine
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.toPx

@Composable
fun TimeTable(
    uiState: PlanUiState,
    onBlockMoved: (String, Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val minuteHeightPx = MINUTE_HEIGHT_DP.dp.toPx(density)

    val engine = remember {
        TimeLayoutEngine(minuteHeightPx)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .background(color = MemoripTheme.colors.background),
    ) {
        Box {
            Row {
                TimeAxis()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = (MINUTES_PER_DAY * MINUTE_HEIGHT_DP).dp)
                ) {
                    uiState.blocks.forEach { block ->
                        TimeBlockItem(
                            block = block,
                            engine = engine,
                            onMoved = onBlockMoved
                        )
                    }
                }
            }
            HorizontalTimeGridLines(
                totalMinutes = MINUTES_PER_DAY,
                majorIntervalMinutes = 60,
                minuteHeightPx = minuteHeightPx
            )
        }
    }
}

@Preview(
    showBackground = true,
    heightDp = 800
)
@Composable
private fun TimeTablePreview() {
    var previewState by remember {
        mutableStateOf(
            PlanUiState(blocks = DummyData.timeBlocks)
        )
    }

    TimeTable(
        uiState = previewState,
        onBlockMoved = { id, newStartMinute ->
            previewState = previewState.copy(
                blocks = previewState.blocks.map { block ->
                    if (block.id == id) {
                        block.copy(startMinute = newStartMinute)
                    } else block
                }
            )
        }
    )
}

@Composable
private fun HorizontalTimeGridLines(
    totalMinutes: Int,
    majorIntervalMinutes: Int,
    minuteHeightPx: Float,
) {
    val lineColor = MemoripTheme.colors.lightGray
    val strokeDp = MemoripLineWidth.TimeTick

    Canvas(modifier = Modifier.fillMaxSize()) {
        var minute = 0
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






