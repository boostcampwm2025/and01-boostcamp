package com.andone.memorip.presentation.screen.plan.component

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.PlanBlockUiModel
import com.andone.memorip.presentation.screen.plan.component.TimeBlockItemConstants.DELETE_THRESHOLD
import com.andone.memorip.presentation.screen.plan.component.TimeBlockItemConstants.SNAP_MINUTE_UNIT
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.screen.plan.utill.TimeLayoutEngine
import com.andone.memorip.presentation.theme.MemoripDragConstants.DRAG_SCALE
import com.andone.memorip.presentation.theme.MemoripDragConstants.DRAG_SHADOW_ELEVATION
import com.andone.memorip.presentation.theme.MemoripDragConstants.DRAG_Z_INDEX
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.toPx
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.roundToInt

object TimeBlockItemConstants {
    const val SNAP_MINUTE_UNIT = 60
    const val DELETE_THRESHOLD = 200
}

@Composable
fun TimeBlockItem(
    block: PlanBlockUiModel,
    engine: TimeLayoutEngine,
    startDate: LocalDate,
    scrollState: ScrollState,
    onMoved: (String, Int) -> Unit,
    onSlide: (String) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    when(block) {
        is Place -> {
            var dragOffsetY by remember { mutableFloatStateOf(value = 0f) }
            var dragOffsetX by remember(block) { mutableFloatStateOf(value = 0f) }
            val startYPx = remember(block) { engine.blockStartYPx(startDate, block) }
            Log.d("DEBUG TEST", "block : $block / startY : $startYPx")
            var isDragging by remember { mutableStateOf(value = false) }

            Box(
                modifier = modifier
                    .offset {
                        IntOffset(
                            x = 0,
                            y = (startYPx + dragOffsetY).roundToInt()
                        )
                    }
                    .fillMaxWidth()
                    .height(height = (block.durationMinutes * MINUTE_HEIGHT_DP).dp)
                    .padding(MemoripPadding.PaddingXSmall)
                    .graphicsLayer {
                        if (isDragging) {
                            scaleX = DRAG_SCALE
                            scaleY = DRAG_SCALE
                            shadowElevation = DRAG_SHADOW_ELEVATION
                            shape = memoripShapes.roundedMedium
                            clip = true
                        }
                    }
                    .zIndex(zIndex = if (isDragging) DRAG_Z_INDEX else 0f)
                    .background(
                        color = MemoripTheme.colors.primaryContainer,
                        shape = memoripShapes.roundedMedium
                    )
            ) {
                Box(
                    modifier = Modifier.fillMaxHeight()
                        .width(dragOffsetX.coerceAtLeast(0f).dp)
                        .background(
                            color = MemoripTheme.colors.error,
                            shape = MemoripTheme.shapes.roundedMedium
                        )
                        .padding(all = MemoripPadding.PaddingMedium)
                        .graphicsLayer{
                            scaleX = 0.8f + (dragOffsetX / DELETE_THRESHOLD).coerceIn(0f, 1f) * 0.2f
                            scaleY = 0.8f + (dragOffsetX / DELETE_THRESHOLD).coerceIn(0f, 1f) * 0.2f
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_outline_delete_24),
                        contentDescription = stringResource(R.string.plan_delete),
                        modifier = Modifier.size(size = MemoripIconSize.IconSizeLarge),
                        tint = MemoripTheme.colors.white
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset {
                            IntOffset(
                                x = dragOffsetX.roundToInt(),
                                y = 0
                            )
                        }
                        .pointerInput(startYPx) {
                            detectDragGesturesAfterLongPress(
                                onDragStart = {
                                    isDragging = true
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    dragOffsetY += dragAmount.y
                                },
                                onDragEnd = {
                                    val rawYPx = startYPx + dragOffsetY
                                    val clampedYPx = rawYPx.coerceAtLeast(minimumValue = 0f)
                                    val newStartMinute = engine.yPxToStartMinute(clampedYPx)
                                    val snappedMinute =
                                        ((newStartMinute + SNAP_MINUTE_UNIT / 2) / SNAP_MINUTE_UNIT) * SNAP_MINUTE_UNIT

                                    dragOffsetY = 0f
                                    isDragging = false
                                    onMoved(block.id, snappedMinute)
                                },
                                onDragCancel = {
                                    dragOffsetY = 0f
                                    isDragging = false
                                }
                            )

                        }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    dragOffsetX += dragAmount
                                    if (dragOffsetX < 0) dragOffsetX = 0f
                                },
                                onDragEnd = {
                                    if (dragOffsetX > DELETE_THRESHOLD) {
                                        onSlide(block.id)
                                    } else {
                                        dragOffsetX = 0f
                                    }
                                },
                                onDragCancel = {
                                    dragOffsetX = 0f
                                }
                            )
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    content()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeBlockItemPreview() {
    val density = LocalDensity.current

    val minuteHeightPx = MINUTE_HEIGHT_DP.dp.toPx(density)

    val engine = remember {
        TimeLayoutEngine(minuteHeightPx)
    }

    TimeBlockItem(
        block = DummyData.places[0],
        startDate = LocalDate.now(),
        engine = engine,
        scrollState = rememberScrollState(),
        onMoved = { _, _ -> },
        onSlide = {},
        content = {}
    )
}
