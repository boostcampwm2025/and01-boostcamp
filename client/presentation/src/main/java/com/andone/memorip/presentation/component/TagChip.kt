package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
    val RADIUS: Dp = 50.dp
    const val BACKGROUND_COLOR_ALPHA: Float = 0.5f
    const val COLOR_LUMINANCE_THRESHOLD: Float = 0.5f
}

@Composable
fun TagChip(
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
        radius = TagChipDimen.RADIUS,
        colors = StaticChipColors(
            backgroundColor = tag.color.copy(alpha = TagChipDimen.BACKGROUND_COLOR_ALPHA),
            textColor = textColor
        )
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
private fun TagChipPreview() {
    MemoripTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DummyData.categories.forEach {
                TagChip(it)
            }
        }
    }
}