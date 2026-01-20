package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.screen.plan.model.TimeBlock
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.screen.plan.utill.TimeLayoutEngine
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun PlanTimeTable(
    uiState: PlanUiState,
    onBlockMoved: (String, Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val minuteHeightPx = with(receiver = density) {
        MINUTE_HEIGHT_DP.dp.toPx()
    }

    val engine = remember {
        TimeLayoutEngine(minuteHeightPx)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .background(color = MemoripTheme.colors.background),
    ) {
        Row {
            TimeAxis()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((MINUTES_PER_DAY * MINUTE_HEIGHT_DP).dp)
            ) {
                TimeGridBackground()
                VerticalGridLines()
                uiState.blocks.forEach { block ->
                    TimeBlockItem(
                        block = block,
                        engine = engine,
                        onMoved = onBlockMoved
                    )
                }
            }
        }
    }
}

@Preview(
    name = "PlanTimeTable Preview",
    showBackground = true,
    heightDp = 800
)
@Composable
private fun PlanTimeTablePreview() {
    var previewState by remember {
        mutableStateOf(
            PlanUiState(
                blocks = listOf(
                    TimeBlock("0", 0, 60),
                    TimeBlock("1", 9 * 60, 60),
                    TimeBlock("2", 11 * 60 + 30, 90),
                    TimeBlock("3", 15 * 60, 45)
                )
            )
        )
    }

    PlanTimeTable(
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
fun TimeAxis() {
    Column(
        modifier = Modifier
            .width(56.dp)
            .height((MINUTES_PER_DAY * MINUTE_HEIGHT_DP).dp)
    ) {
        for (hour in 0 until 24) {
            Box(
                modifier = Modifier.height((60 * MINUTE_HEIGHT_DP).dp)
            ) {
                Text(
                    text = String.format("%02d:00", hour),
                    color = MemoripTheme.colors.onSurface
                )
            }
        }
    }
}

@Composable
fun TimeGridBackground() {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(times = 24) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = (60 * MINUTE_HEIGHT_DP).dp)
            ) {
                repeat(times = 6) { index ->
                    val minute = index * 10

                    val (color, thickness) = when (minute) {
                        0 -> MemoripTheme.colors.gray to 1.2.dp
                        30 -> MemoripTheme.colors.gray to 0.7.dp
                        else -> MemoripTheme.colors.gray to 0.4.dp
                    }

                    GridLine(
                        y = minute,
                        color = color,
                        thickness = thickness
                    )
                }
            }
        }
    }
}

@Composable
private fun GridLine(
    y: Int,
    color: Color,
    thickness: Dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = thickness)
            .offset(y = (y * MINUTE_HEIGHT_DP).dp)
            .background(color)
    )
}

@Composable
fun VerticalGridLines() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 0.5.dp,
                color = Color.LightGray
            )
    )
}




