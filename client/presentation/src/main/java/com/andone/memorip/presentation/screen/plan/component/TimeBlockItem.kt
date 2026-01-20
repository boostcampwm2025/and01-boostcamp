package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.andone.memorip.presentation.screen.plan.component.TimeBlockItemConstants.SNAP_MINUTE_UNIT
import com.andone.memorip.presentation.screen.plan.model.TimeBlock
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.screen.plan.utill.TimeLayoutEngine
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import com.andone.memorip.presentation.util.toPx
import kotlin.math.roundToInt
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripDragConstants.DRAG_SCALE
import com.andone.memorip.presentation.theme.MemoripDragConstants.DRAG_SHADOW_ELEVATION
import com.andone.memorip.presentation.theme.MemoripDragConstants.DRAG_Z_INDEX

private object TimeBlockItemConstants {
    val SNAP_MINUTE_UNIT = 60
}

@Composable
fun TimeBlockItem(
    block: TimeBlock,
    engine: TimeLayoutEngine,
    onMoved: (String, Int) -> Unit,
) {
    var dragOffsetY by remember { mutableFloatStateOf(value = 0f) }
    val startYPx = engine.blockStartYPx(block)
    var isDragging by remember { mutableStateOf(value = false) }

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = (startYPx + dragOffsetY).roundToInt()
                )
            }
            .fillMaxWidth()
            .height((block.durationMinute * MINUTE_HEIGHT_DP).dp)
            .padding(MemoripPadding.PaddingSmall)
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
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {

            }

            Icon(
                painter = painterResource(R.drawable.ic_outline_drag_handle_24),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = MemoripPadding.PaddingSmall)
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
                                val absoluteYPx = startYPx + dragOffsetY
                                val newStartMinute =
                                    engine.yPxToStartMinute(absoluteYPx)

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
            )
        }
    }
}


@Preview(
    name = "TimeBlockItem Preview",
    showBackground = true,
    widthDp = 360,
    heightDp = 200
)
@Composable
fun TimeBlockItemPreview() {
    val density = LocalDensity.current

    val minuteHeightPx = MINUTE_HEIGHT_DP.dp.toPx(density)

    val engine = remember {
        TimeLayoutEngine(minuteHeightPx)
    }

    TimeBlockItem(
        block = TimeBlock(
            id = "preview",
            startMinute = 0,
            durationMinute = 60
        ),
        engine = engine,
        onMoved = { _, _ -> }
    )
}
