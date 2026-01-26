package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun EmptyText(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MemoripTheme.colors.gray,
            style = MemoripTheme.typography.bodyBold16
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyTextPreview() {
    EmptyText(
        text = "결과가 없습니다.",
        modifier = Modifier.fillMaxSize()
    )
}