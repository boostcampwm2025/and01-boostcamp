package com.andone.memorip.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object StaticChipDimen {
    val RADIUS: Dp = 50.dp
}

@Immutable
data class StaticChipColors(
    val backgroundColor: Color,
    val textColor: Color,
    val borderColor: Color? = null
) {
    companion object {
        val Default: StaticChipColors
            @Composable
            get() = StaticChipColors(
                backgroundColor = MemoripTheme.colors.offWhite,
                textColor = MemoripTheme.colors.onOffWhite,
                borderColor = null
            )
    }
}

@Composable
fun StaticChip(
    chipName: String,
    modifier: Modifier = Modifier,
    radius: Dp = StaticChipDimen.RADIUS,
    colors: StaticChipColors = StaticChipColors.Default
) {
    val borderModifier = if (colors.borderColor != null) {
        modifier.border(
            width = MemoripBorderWidth.Thin,
            color = colors.borderColor,
            shape = RoundedCornerShape(radius)
        )
    } else { modifier }

    Surface(
        modifier = modifier.then(other = borderModifier),
        shape = RoundedCornerShape(size = radius),
        color = colors.backgroundColor,
    ) {
        Text(
            text = chipName,
            modifier = Modifier.padding(
                horizontal = MemoripPadding.PaddingSmall,
                vertical = MemoripPadding.PaddingXXSmall
            ),
            style = MemoripTheme.typography.label1,
            color = colors.textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StaticChipPreview() {
    MemoripTheme {
        Column {
            StaticChip(chipName = "태그태그태그태그태그")
            StaticChip(
                chipName = "느좋",
                colors = StaticChipColors.Default.copy(
                    borderColor = MemoripTheme.colors.primary
                )
            )
            StaticChip(
                chipName = "카페",
                radius = 10.dp,
                colors = StaticChipColors.Default.copy(
                    borderColor = MemoripTheme.colors.primary
                )
            )
        }
    }
}