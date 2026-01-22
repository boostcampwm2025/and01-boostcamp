package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
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
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.plan.component.TimeBlockItemConstants.SNAP_MINUTE_UNIT
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.DEFAULT_ALPHA
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.DEFAULT_ZINDEX
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.FULL_WEIGHT
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.PICKED_ALPHA
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.PICKED_ELEVATION
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.PICKED_SCALE
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.PICKED_ZINDEX
import com.andone.memorip.presentation.screen.plan.component.TimeTableConstants.SCROLL_DURATION
import com.andone.memorip.presentation.screen.plan.model.DraggablePlace
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
    const val PICKED_ELEVATION = 12f
    const val PICKED_ZINDEX = 1f
    const val DEFAULT_ZINDEX = 0f
    const val PICKED_ALPHA = 0.3f
    const val DEFAULT_ALPHA = 1f
}

@Composable
fun TimeTable(
    totalMinutes: Int,
    currentDay: Int?,
    onBlockAdd: (TimeBlock) -> Unit,
    onDayScrolled: (Int) -> Unit = {},
    content: @Composable (TimeLayoutEngine, ScrollState) -> Unit
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val minuteHeightPx = MINUTE_HEIGHT_DP.dp.toPx(density)
    val engine = remember(minuteHeightPx) {
        TimeLayoutEngine(minuteHeightPx)
    }
    val places = remember { DummyData.places.toMutableStateList() }
    val backgroundShape = MemoripTheme.shapes.roundedMedium
    var rowTop by remember { mutableStateOf(0f) }
    var timeTableTop by remember { mutableStateOf(0f) }
    var selectedPlace by remember { mutableStateOf<DraggablePlace?>(null) }
    var rootCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
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
                            content(engine, scrollState)
                        }
                    }
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
                    var itemTop by remember { mutableStateOf(value = 0f) }
                    var itemBottom by remember { mutableStateOf(value = 0f) }
                    var relativePos by remember { mutableStateOf(value = Offset.Zero) }
                    var isDraggable by remember { mutableStateOf(false) }
                    PlacePickerItem(
                        place = place,
                        modifier = Modifier
                            .onGloballyPositioned { layout ->
                                val pos = layout.positionInRoot()
                                itemTop = pos.y
                                itemBottom = pos.y + layout.size.height
                                relativePos = rootCoordinates?.localPositionOf(layout, Offset.Zero)
                                    ?: Offset.Zero
                            }
                            .background(
                                color = MemoripTheme.colors.primaryContainer,
                                shape = backgroundShape
                            )
                            .pointerInput(key1 = place.id) {
                                currentDay?.let {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            selectedPlace = DraggablePlace(
                                                place = place,
                                                originPos = relativePos,
                                                offset = Offset.Zero
                                            )
                                            isDraggable = true
                                        },
                                        onDragEnd = {
                                            selectedPlace?.let { selectedPlace ->
                                                val isRowInside =
                                                    (itemBottom + selectedPlace.offset.y) >= rowTop
                                                if (!isRowInside) {
                                                    val absoluteYPx =
                                                        ((itemTop + itemBottom) / 2) + selectedPlace.offset.y + scrollState.value - timeTableTop
                                                    val newStartMinute =
                                                        engine.yPxToStartMinute(absoluteYPx)
                                                    val snappedMinute =
                                                        ((newStartMinute + SNAP_MINUTE_UNIT / 2) / SNAP_MINUTE_UNIT) * SNAP_MINUTE_UNIT
                                                    onBlockAdd(createNewBlock(place, snappedMinute))
                                                    places.remove(place)
                                                }
                                            }
                                            selectedPlace = null
                                            isDraggable = false
                                        },
                                        onDragCancel = {
                                            selectedPlace = null
                                            isDraggable = false
                                        },
                                        onDrag = { change, amount ->
                                            change.consume()
                                            selectedPlace?.let { place ->
                                                selectedPlace =
                                                    place.copy(offset = place.offset + amount)
                                            }
                                        }
                                    )
                                }
                            }
                            .zIndex(zIndex = if (isDraggable) PICKED_ZINDEX else DEFAULT_ZINDEX)
                            .alpha(alpha = if (isDraggable) PICKED_ALPHA else DEFAULT_ALPHA)
                    )
                }
            }
        }
        if (selectedPlace != null) {
            val place = selectedPlace!!
            PlacePickerItem(
                place = place.place,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = (place.originPos.x + place.offset.x).toInt(),
                            y = (place.originPos.y + place.offset.y).toInt()
                        )
                    }
                    .graphicsLayer {
                        scaleX = PICKED_SCALE
                        scaleY = PICKED_SCALE
                        shadowElevation = PICKED_ELEVATION
                        shape = backgroundShape
                    }
            )
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
    TimeTable(
        totalMinutes = MINUTES_PER_DAY,
        currentDay = 1,
        onBlockAdd = {},
        content = { _, _ -> },
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