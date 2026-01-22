package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object TagChipDimen {
    val STATIC_CHIP_RADIUS: Dp = 6.dp
    val CLICKABLE_CHIP_RADIUS: Dp = 50.dp
    const val BACKGROUND_COLOR_ALPHA: Float = 0.5f
    const val COLOR_LUMINANCE_THRESHOLD: Float = 0.5f
}

@Composable
fun StaticTagChip(
    tag: TagUiModel,
    modifier: Modifier = Modifier,
) {
    val textColor = if (tag.color.luminance() > TagChipDimen.COLOR_LUMINANCE_THRESHOLD) {
        MemoripTheme.colors.black
    } else {
        MemoripTheme.colors.white
    }

    StaticChip(
        chipName = tag.name,
        modifier = modifier,
        radius = TagChipDimen.STATIC_CHIP_RADIUS,
        colors = ChipColors(
            backgroundColor = tag.color.copy(alpha = TagChipDimen.BACKGROUND_COLOR_ALPHA),
            textColor = textColor
        )
    )
}

@Composable
fun ClickableTagChip(
    tag: TagUiModel,
    modifier: Modifier = Modifier,
) {
    val textColor = if (tag.color.luminance() > TagChipDimen.COLOR_LUMINANCE_THRESHOLD) {
        MemoripTheme.colors.black
    } else {
        MemoripTheme.colors.white
    }

    ClickableChip(
        chipName = tag.name,
        modifier = modifier,
        radius = TagChipDimen.CLICKABLE_CHIP_RADIUS,
        colors = ChipColors(
            backgroundColor = tag.color.copy(alpha = TagChipDimen.BACKGROUND_COLOR_ALPHA),
            textColor = textColor
        )
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
private fun TagChipPreview() {
    MemoripTheme {
        Column {
            Row {
                DummyData.categories.forEach {
                    StaticTagChip(it)
                }
            }
            Row {
                DummyData.categories.forEach {
                    ClickableTagChip(it)
                }
            }
        }
    }
}