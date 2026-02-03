package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripAlpha
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun DayChip(
    day: Int,
    modifier: Modifier = Modifier,
    selected: Boolean = true,
    isDeleteMode: Boolean = false,
    isDeletedTarget: Boolean = false,
    onClick: (Int) -> Unit = {},
    onLongClick: (Int) -> Unit = {},
) {
    val backgroundColor = when {
        isDeleteMode && isDeletedTarget -> MemoripTheme.colors.error
        !isDeleteMode && selected -> MemoripTheme.colors.primary
        else -> MemoripTheme.colors.primaryContainer
    }
    val textColor = when {
        isDeleteMode && isDeletedTarget -> MemoripTheme.colors.white
        !isDeleteMode && selected -> MemoripTheme.colors.white
        else -> MemoripTheme.colors.onSurface
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
            Text(
                text = stringResource(id = R.string.plan_day_format, day),
                color = textColor,
                style = MemoripTheme.typography.titleMedium14
            )
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