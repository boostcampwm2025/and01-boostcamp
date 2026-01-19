package com.andone.memorip.presentation.screen.selectgroup.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun GroupImageGridCard(
    name: String,
    images: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        GroupImageGridMax3(
            images = images,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MemoripSpace.SpaceXXSmall),
            style = MemoripTheme.typography.title2,
            color = MemoripTheme.colors.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun GroupImageGridCardPreview() {
    MemoripTheme {
        GroupImageGridCard(
            name = "겨울 여행",
            images = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg",
            ),
            onClick = {},
        )
    }
}