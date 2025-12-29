package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.selectcategory.model.Category
import com.andone.memorip.presentation.theme.MemoripTheme

private object CategoryChipDimen {
    val RADIUS: Dp = 50.dp
    const val BACKGROUND_COLOR_ALPHA: Float = 0.1f
}

@Composable
fun CategoryChip(
    category: Category,
    modifier: Modifier = Modifier,
) {
    StaticChip(
        chipName = category.category,
        modifier = modifier,
        radius = CategoryChipDimen.RADIUS,
        colors = StaticChipColors(
            backgroundColor = category.color.copy(alpha = CategoryChipDimen.BACKGROUND_COLOR_ALPHA),
            textColor = category.color
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CategoryChipPreview() {
    MemoripTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryChip(
                category = Category(
                    id = 1L,
                    category = "맛집",
                    color = Color(0xFFE53935)
                )
            )
            CategoryChip(
                category = Category(
                    id = 2L,
                    category = "카페",
                    color = Color(0xFF8D6E63)
                )
            )
            CategoryChip(
                category = Category(
                    id = 3L,
                    category = "관광지",
                    color = Color(0xFF1E88E5)
                )
            )
            CategoryChip(
                category = Category(
                    id = 4L,
                    category = "숙소",
                    color = Color(0xFF43A047)
                )
            )
            CategoryChip(
                category = Category(
                    id = 5L,
                    category = "쇼핑",
                    color = Color(0xFF9C27B0)
                )
            )
        }
    }
}