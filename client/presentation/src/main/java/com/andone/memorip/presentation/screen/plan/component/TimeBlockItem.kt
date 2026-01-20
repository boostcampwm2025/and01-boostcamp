package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.screen.plan.component.TimeBlockItemConstants.SNAP_MINUTE_UNIT
import com.andone.memorip.presentation.screen.plan.model.TimeBlock
import com.andone.memorip.presentation.screen.plan.utill.MINUTE_HEIGHT_DP
import com.andone.memorip.presentation.screen.plan.utill.TimeLayoutEngine
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import com.andone.memorip.presentation.util.toPx
import kotlin.math.roundToInt

private object TimeBlockItemConstants {
    val SNAP_MINUTE_UNIT = 10
}

@Composable
fun TimeBlockItem(
    block: TimeBlock,
    engine: TimeLayoutEngine,
    onMoved: (String, Int) -> Unit
) {
    var dragOffsetY by remember { mutableFloatStateOf(value = 0f) }

    val startYPx = engine.blockStartYPx(block)

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = (startYPx + dragOffsetY).roundToInt()
                )
            }
            .fillMaxWidth()
            .height(height = (block.durationMinute * MINUTE_HEIGHT_DP).dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = MemoripPadding.PaddingXXXSmall)
                .clip(shape = memoripShapes.roundedMedium)
                .background(color = MemoripTheme.colors.primaryContainer)
                .pointerInput(startYPx) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            dragOffsetY += dragAmount.y
                        },
                        onDragEnd = {
                            val absoluteYPx = startYPx + dragOffsetY
                            val newStartMinute = engine.yPxToStartMinute(absoluteYPx)
                            val snappedMinute =
                                ((newStartMinute + SNAP_MINUTE_UNIT / 2) / SNAP_MINUTE_UNIT) * SNAP_MINUTE_UNIT

                            dragOffsetY = 0f
                            onMoved(block.id, snappedMinute)
                        }
                    )
                }
        )
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
