package com.andone.memorip.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object StaticChipDimen {
    val RADIUS: Dp = 50.dp
}

@Immutable
data class ChipColors(
    val backgroundColor: Color,
    val textColor: Color,
    val borderColor: Color? = null
) {
    companion object {
        val Default: ChipColors
            @Composable
            get() = ChipColors(
                backgroundColor = MemoripTheme.colors.primaryContainer,
                textColor = MemoripTheme.colors.onSurface,
                borderColor = null
            )

        val Selected: ChipColors
            @Composable
            get() = ChipColors(
                backgroundColor = MemoripTheme.colors.primaryContainer,
                textColor = MemoripTheme.colors.onSurface,
                borderColor = MemoripTheme.colors.primary
            )
    }
}

@Composable
fun StaticChip(
    chipName: String,
    modifier: Modifier = Modifier,
    radius: Dp = StaticChipDimen.RADIUS,
    colors: ChipColors = ChipColors.Default,
    textStyle: TextStyle = MemoripTheme.typography.label2,
    elevation: Dp = 0.dp
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(radius),
        color = colors.backgroundColor,
        shadowElevation = elevation,
        border = colors.borderColor?.let {
            BorderStroke(
                width = MemoripLineWidth.Thin,
                color = it
            )
        }
    ) {
        Text(
            text = chipName,
            modifier = Modifier.padding(
                horizontal = MemoripPadding.PaddingXXSmall,
                vertical = MemoripPadding.PaddingXXXSmall
            ),
            style = textStyle,
            color = colors.textColor
        )
    }
}

@Composable
fun ClickableChip(
    chipName: String,
    modifier: Modifier = Modifier,
    radius: Dp = StaticChipDimen.RADIUS,
    colors: ChipColors = ChipColors.Default,
    textStyle: TextStyle = MemoripTheme.typography.label1,
    elevation: Dp = 0.dp,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(radius),
        color = colors.backgroundColor,
        shadowElevation = elevation,
        onClick = onClick,
        border = colors.borderColor?.let {
            BorderStroke(
                width = MemoripLineWidth.Thin,
                color = it
            )
        }
    ) {
        Text(
            text = chipName,
            modifier = Modifier.padding(
                horizontal = MemoripPadding.PaddingSmall,
                vertical = MemoripPadding.PaddingXXSmall
            ),
            style = textStyle,
            color = colors.textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChipPreview() {
    MemoripTheme {
        Column {
            StaticChip(chipName = "태그태그태그태그태그")
            StaticChip(
                chipName = "느좋",
                colors = ChipColors.Default.copy(
                    borderColor = MemoripTheme.colors.primary
                )
            )
            StaticChip(
                chipName = "카페",
                radius = 10.dp,
                colors = ChipColors.Default.copy(
                    borderColor = MemoripTheme.colors.primary
                )
            )
            ClickableChip(
                chipName = "카페",
                radius = 50.dp,
                colors = ChipColors.Default.copy(
                    borderColor = MemoripTheme.colors.primary
                )
            )
        }
    }
}