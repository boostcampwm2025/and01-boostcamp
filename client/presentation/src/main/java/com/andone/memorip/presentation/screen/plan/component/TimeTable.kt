package com.andone.memorip.presentation.screen.plan.component

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.andone.memorip.presentation.screen.plan.model.TimeBlock
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.screen.plan.utill.TimeLayoutEngine
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.toPx

@Composable
fun TimeTable(
    blocks: List<TimeBlock>,
    onBlockMoved: (String, Int) -> Unit
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val minuteHeightPx = MINUTE_HEIGHT_DP.dp.toPx(density)

    val engine = remember {
        TimeLayoutEngine(minuteHeightPx)
    }

    val places = remember{ DummyData.places.toMutableStateList() }
    var rowBounds by remember{ mutableStateOf<Rect?>(null) }
    var rowTop by remember { mutableStateOf(0f) }
    var rowBottom by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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
                        blocks.forEach { block ->
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
        LazyRow(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .padding(horizontal = MemoripPadding.PaddingMedium)
                .onGloballyPositioned { layout ->
                    val position = layout.positionInRoot()
                    rowTop = position.y
                    rowBottom = position.y + layout.size.height
                    rowBounds = Rect(
                        position,
                        layout.size.toSize()
                    )
                },
            horizontalArrangement = Arrangement.spacedBy(MemoripPadding.PaddingXSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                items = places,
                key = { it.id }
            ) { place ->
                var offset by remember{ mutableStateOf(Offset.Zero) }
                var originOffset by remember{ mutableStateOf(Offset.Zero) }
                var isDragging by remember{ mutableStateOf(false) }
                var itemTopY by remember{ mutableStateOf(0f) }
                var itemBottomY by remember{ mutableStateOf(0f) }
                val animatedOffset by animateOffsetAsState(
                    targetValue = offset
                )
                val scale by animateFloatAsState(
                    targetValue = if (isDragging) 0.8f else 1f,
                    label = "catch animation"
                )

                Box(
                    modifier = Modifier
                        .onGloballyPositioned { layout ->
                            val pos = layout.positionInRoot()
                            itemTopY = pos.y
                            itemBottomY = pos.y + layout.size.height
                        }
                        .offset {
                            IntOffset(animatedOffset.x.toInt(), animatedOffset.y.toInt())
                        }
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            shadowElevation = if (isDragging) 12f else 0f
                        }
                        .width(width = 80.dp)
                        .height(height = 100.dp)
                        .background(
                            color = MemoripTheme.colors.primaryContainer,
                            shape = MemoripTheme.shapes.roundedMedium
                        )
                        .pointerInput(key1 = place.id) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = {
                                    isDragging = true
                                    originOffset = offset
                                },
                                onDragEnd = {
                                    isDragging = false

                                    val isRowInside = (itemBottomY + offset.y) >= rowTop
                                    if (isRowInside) {
                                        offset = originOffset
                                    } else {
                                        places.remove(place)
                                    }
                                },
                                onDragCancel = { isDragging = false },
                                onDrag = { change, amount ->
                                    change.consume()
                                    offset += amount
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = place.name
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeTablePreview() {
    var previewState by remember {
        mutableStateOf(value = DummyData.timeBlocks)
    }

    TimeTable(
        blocks = previewState,
        onBlockMoved = { id, newStartMinute -> }
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