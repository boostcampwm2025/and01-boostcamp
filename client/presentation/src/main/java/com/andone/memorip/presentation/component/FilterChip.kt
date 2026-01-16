package com.andone.memorip.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun FilterChip(
    text: String,
    selected: Boolean = true
) {
    Box(
        modifier = Modifier
            .background(
                color = MemoripTheme.colors.primaryContainer,
                shape = memoripShapes.roundedLarge
            )
            .padding(
                horizontal = MemoripPadding.PaddingSmall,
                vertical = MemoripPadding.PaddingXSmall
            )
    ) {
        Text(
            text = text,
            color = if (selected)
                MemoripTheme.colors.onSurface
            else
                /** TODO MemoripTheme으로 변경하기 */
                MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (selected)
                FontWeight.Bold
            else
                FontWeight.Normal
        )
    }
}

@Preview
@Composable
private fun FilterChipPreview() {
    MemoripTheme {
        FilterChip(text = "전체")
    }
}