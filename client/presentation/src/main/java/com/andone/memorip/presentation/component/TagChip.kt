package com.andone.memorip.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.model.Category
import com.andone.memorip.presentation.theme.MemoripTheme

private object TagChipDimen {
    val RADIUS: Dp = 50.dp
    const val BACKGROUND_COLOR_ALPHA: Float = 0.5f
    const val COLOR_LUMINANCE_THRESHOLD: Float = 0.5f
}

@Composable
fun TagChip(
    tag: Category,
    modifier: Modifier = Modifier,
) {
    val textColor = if (tag.color.luminance() > TagChipDimen.COLOR_LUMINANCE_THRESHOLD) {
        MemoripTheme.colors.black
    } else {
        MemoripTheme.colors.white
    }

    StaticChip(
        chipName = tag.category,
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
            TagChip(
                tag = Category(
                    id = 1L,
                    category = "맛집",
                    color = Color(0xFFE53935)
                )
            )
            TagChip(
                tag = Category(
                    id = 2L,
                    category = "카페",
                    color = Color(0xFF8D6E63)
                )
            )
            TagChip(
                tag = Category(
                    id = 3L,
                    category = "관광지",
                    color = Color(0xFF1E88E5)
                )
            )
            TagChip(
                tag = Category(
                    id = 4L,
                    category = "숙소",
                    color = Color(0xFF43A047)
                )
            )
            TagChip(
                tag = Category(
                    id = 5L,
                    category = "쇼핑",
                    color = Color(0xFF9C27B0)
                )
            )
            TagChip(
                tag = Category(
                    id = 6L,
                    category = "밝은색",
                    color = Color(0xFFFFEB3B)
                )
            )
            TagChip(
                tag = Category(
                    id = 7L,
                    category = "검은색",
                    color = Color(0xFF222222)
                )
            )
            TagChip(
                tag = Category(
                    id = 8L,
                    category = "흰색",
                    color = Color(0xFFFFFFFF)
                )
            )
        }
    }
}