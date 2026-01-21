package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import kotlin.math.roundToInt
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripAlpha

@Composable
fun DayChip(
    day: Int,
    modifier: Modifier = Modifier,
    selected: Boolean = true,
    onClick: (Int) -> Unit = {},
    onLongClick: (Int) -> Unit = {},
    isDeleteMode: Boolean = false,
    isDeletedTarget: Boolean = false,
) {
    val backgroundColor = when {
        isDeleteMode && isDeletedTarget -> MemoripTheme.colors.error
        !isDeleteMode && selected -> MemoripTheme.colors.primary
        else -> MemoripTheme.colors.primaryContainer
    }

    key(isDeleteMode) {
        Box(
            modifier = modifier
                .clip(shape = memoripShapes.roundedXSmall)
                .background(backgroundColor)
                .combinedClickable(
                    enabled = !isDeleteMode,
                    onClick = { onClick(day) },
                    onLongClick = { onLongClick(day) }
                )
                .alpha(alpha = if (!isDeleteMode || isDeletedTarget) MemoripAlpha.DEFAULT else MemoripAlpha.SCRIM)
                .padding(
                    horizontal = MemoripPadding.PaddingXSmall,
                    vertical = MemoripPadding.PaddingXXSmall
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(id = R.string.plan_day_format, day))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DayChipPreview() {
    MemoripTheme {
        DayChip(
            day = 1,
            selected = true,
        )
    }
}