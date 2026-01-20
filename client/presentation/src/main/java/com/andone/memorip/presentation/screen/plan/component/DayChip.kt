package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import kotlin.math.roundToInt

@Composable
fun DayChip(
    text: String,
    modifier: Modifier = Modifier,
    selected: Boolean = true,
    onClick: () -> Unit = {},
    onDrag: (Offset) -> Unit = {},
    onDragEnd: (Offset) -> Unit = {}
) {
    var dragOffset by remember { mutableStateOf(value = Offset.Zero) }

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    dragOffset.x.roundToInt(),
                    dragOffset.y.roundToInt()
                )
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount
                        onDrag(dragOffset)
                    },
                    onDragEnd = {
                        onDragEnd(dragOffset)
                        dragOffset = Offset.Zero
                    }
                )
            }
            .clip(shape = memoripShapes.roundedXXSmall)
            .background(color = if (selected) MemoripTheme.colors.primary else MemoripTheme.colors.primaryContainer)
            .padding(horizontal = MemoripPadding.PaddingXSmall, vertical = MemoripPadding.PaddingXXSmall),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun DayChipPreview() {
    MemoripTheme {
        DayChip(
            text = "Day 1",
            selected = true,
            onClick = {},
            onDrag = {}
        )
    }
}