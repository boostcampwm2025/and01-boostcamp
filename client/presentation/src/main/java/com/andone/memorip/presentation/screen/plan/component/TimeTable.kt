package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.plan.component.TimeBlockItemConstants.SNAP_MINUTE_UNIT
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.DEFAULT_ELEVATION
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.DEFAULT_SCALE
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.DEFAULT_ZINDEX
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.FULL_WEIGHT
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.PICKED_ELEVATION
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.PICKED_SCALE
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.PICKED_ZINDEX
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.SCALE_ANIMATION
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.SCROLL_DURATION
import com.andone.memorip.presentation.screen.plan.model.DraggablePlace
import com.andone.memorip.presentation.screen.plan.model.TimeBlock
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_HOUR
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.screen.plan.utill.TimeLayoutEngine
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.toPx
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private object TimeTableConstants {
    const val SCROLL_DURATION = 700
    const val FULL_WEIGHT = 1f
    const val PICKED_SCALE = 0.8f
    const val DEFAULT_SCALE = 1f
    const val SCALE_ANIMATION = "Scale Animation"
    const val PICKED_ELEVATION = 12f
    const val DEFAULT_ELEVATION = 0f
    const val PICKED_ZINDEX = 1f
    const val DEFAULT_ZINDEX = 0f
}

@Composable
fun TimeTable(
    blocks: List<TimeBlock>,
    onBlockAdd: (TimeBlock) -> Unit,
    onBlockMoved: (String, Int) -> Unit,
    totalMinutes: Int,
    currentDay: Int?,
    onDayScrolled: (Int) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val minuteHeightPx = MINUTE_HEIGHT_DP.dp.toPx(density)
    val engine = remember(minuteHeightPx) {
        TimeLayoutEngine(minuteHeightPx)
    }

    var isAutoScrolling by remember { mutableStateOf(false) }
    var lastDayFromScroll by remember { mutableStateOf<Int?>(null) }

    val places = remember { DummyData.places.toMutableStateList() }
    var rowTop by remember { mutableStateOf(0f) }
    var timeTableTop by remember { mutableStateOf(0f) }
    var selectedPlace by remember { mutableStateOf<DraggablePlace?>(null) }
    var rootCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

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
            .fillMaxSize()
            .onGloballyPositioned { rootCoordinates = it }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = FULL_WEIGHT)
                    .onGloballyPositioned { timeTableTop = it.positionInRoot().y }
                    .verticalScroll(state = scrollState)
                    .background(color = MemoripTheme.colors.background),
            ) {
                Box {
                    Row {
                        TimeAxis(totalMinutes = totalMinutes)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height = (totalMinutes * MINUTE_HEIGHT_DP).dp)
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
                        totalMinutes = totalMinutes,
                        minuteHeightPx = minuteHeightPx
                    )
                }
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MemoripPadding.PaddingMedium,
                        vertical = MemoripPadding.PaddingSmall
                    )
                    .onGloballyPositioned { layout ->
                        rowTop = layout.positionInRoot().y
                    },
                horizontalArrangement = Arrangement.spacedBy(space = MemoripPadding.PaddingXSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(
                    items = places,
                    key = { it.id }
                ) { place ->
                    var offset by remember { mutableStateOf(value = Offset.Zero) }
                    var itemTop by remember { mutableStateOf(value = 0f) }
                    var itemBottom by remember { mutableStateOf(value = 0f) }
                    var relativePos by remember { mutableStateOf(value = Offset.Zero) }
                    val animatedOffset by animateOffsetAsState(targetValue = offset)
                    val scale by animateFloatAsState(
                        targetValue = if (selectedPlace != null) PICKED_SCALE else DEFAULT_SCALE,
                        label = SCALE_ANIMATION
                    )
                    var idx by remember { mutableStateOf(-1) }

                    Box(
                        modifier = Modifier
                            .onGloballyPositioned { layout ->
                                val pos = layout.positionInRoot()
                                itemTop = pos.y
                                itemBottom = pos.y + layout.size.height
                                relativePos = rootCoordinates?.localPositionOf(layout, Offset.Zero) ?: Offset.Zero
                            }
                            .offset {
                                IntOffset(
                                    x = animatedOffset.x.toInt(),
                                    y = animatedOffset.y.toInt()
                                )
                            }
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                shadowElevation = if (selectedPlace != null) PICKED_ELEVATION else DEFAULT_ELEVATION
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
                                        selectedPlace = DraggablePlace(
                                            place = place,
                                            originPos = relativePos,
                                            offset = Offset.Zero
                                        )
                                        idx = places.indexOf(place)
                                    },
                                    onDragEnd = {
                                        val isRowInside = (itemBottom + offset.y) >= rowTop
                                        if (isRowInside) {
                                            offset = selectedPlace!!.originPos
                                        } else {
                                            val absoluteYPx =
                                                ((itemTop + itemBottom) / 2) + offset.y + scrollState.value - timeTableTop
                                            val newStartMinute =
                                                engine.yPxToStartMinute(absoluteYPx)
                                            val snappedMinute =
                                                ((newStartMinute + SNAP_MINUTE_UNIT / 2) / SNAP_MINUTE_UNIT) * SNAP_MINUTE_UNIT
                                            onBlockAdd(createNewBlock(place, snappedMinute))
                                            places.remove(place)
                                        }
                                        selectedPlace = null
                                    },
                                    onDragCancel = {
                                        selectedPlace = null
                                    },
                                    onDrag = { change, amount ->
                                        change.consume()
                                        offset += amount
                                        selectedPlace?.let { place ->
                                            selectedPlace =
                                                place.copy(offset = place.offset + amount)
                                        }
                                    }
                                )
                            }
                            .zIndex(zIndex = if (selectedPlace != null) PICKED_ZINDEX else DEFAULT_ZINDEX)
                            .alpha(alpha = if (selectedPlace != null && selectedPlace!!.place == place) 0.3f else 1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = place.name
                        )
                    }
                }
            }
        }
        if (selectedPlace != null) {
            val place = selectedPlace!!
            Box(
                modifier = Modifier
                    .width(width = 80.dp)
                    .height(height = 100.dp)
                    .offset {
                        IntOffset(
                            (place.originPos.x + place.offset.x).toInt(),
                            (place.originPos.y + place.offset.y).toInt()
                        )
                    }
                    .graphicsLayer {
                        scaleX = PICKED_SCALE
                        scaleY = PICKED_SCALE
                        shadowElevation = PICKED_ELEVATION
                    }
                    .background(
                        color = MemoripTheme.colors.black,
                        shape = MemoripTheme.shapes.roundedMedium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = place.place.name,
                    color = MemoripTheme.colors.white
                )
            }
        }
    }
}

private fun createNewBlock(place: Place, startMinute: Int): TimeBlock {
    return TimeBlock(
        id = place.id,
        startMinute = startMinute,
        durationMinute = 60,
    )
}

@Preview(showBackground = true)
@Composable
private fun TimeTablePreview() {
    var previewState by remember {
        mutableStateOf(value = DummyData.timeBlocks)
    }

    TimeTable(
        blocks = previewState,
        totalMinutes = MINUTES_PER_DAY,
        currentDay = 1,
        onBlockMoved = { id, newStartMinute -> },
        onBlockAdd = { timeBlock -> },
        onDayScrolled = { value -> }
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