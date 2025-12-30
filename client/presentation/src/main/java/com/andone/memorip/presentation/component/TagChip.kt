package com.andone.memorip.presentation.component

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.andone.memorip.presentation.selectcategory.model.Category
import com.andone.memorip.presentation.theme.MemoripTheme

private object TagChipDimen {
    val RADIUS: Dp = 50.dp
    const val BACKGROUND_COLOR_ALPHA: Float = 0.15f
}

@Composable
fun TagChip(
    tag: Category,
    modifier: Modifier = Modifier,
) {
    val isDark = isSystemInDarkTheme()
    val adaptiveTextColor = getAdaptiveToneColor(tag.color, isDark)

    StaticChip(
        chipName = tag.category,
        modifier = modifier,
        radius = TagChipDimen.RADIUS,
        colors = StaticChipColors(
            backgroundColor = tag.color.copy(alpha = TagChipDimen.BACKGROUND_COLOR_ALPHA),
            textColor = adaptiveTextColor
        )
    )
}

private fun getAdaptiveToneColor(baseColor: Color, isDark: Boolean): Color {
    val hsl = FloatArray(3) // HSL = 색조(H) + 채도(S) + 명도(L)

    ColorUtils.colorToHSL(baseColor.toArgb(), hsl)

    if (isDark) {
        hsl[2] = 0.75f
        hsl[1] = Math.max(hsl[1], 0.5f)
    } else {
        hsl[2] = 0.35f
        hsl[1] = Math.max(hsl[1], 0.7f)
    }

    return Color(ColorUtils.HSLToColor(hsl))
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
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