package com.andone.memorip.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.domain.group.buildBento5x3Items
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun GroupView(
    name: String,
    images: List<String>,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
    ) {
        Text(name)
        GroupLayout(
            items = buildBento5x3Items(images),
            modifier = Modifier
                .fillMaxWidth()
                .background(MemoripTheme.colors.white)
        ) { item ->
            ImageCard(item)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GroupViewPreview() {
    MemoripTheme {
        GroupView(
            name = "기본 그룹",
            images = listOf(
                "https://picsum.photos/300/300?1",
                "https://picsum.photos/300/300?1",
                "https://picsum.photos/300/300?1",
                "https://picsum.photos/300/300?1",
                "https://picsum.photos/300/300?1",
                "https://picsum.photos/300/300?1",
                "https://picsum.photos/300/300?1",
                "https://picsum.photos/300/300?1",
                "https://picsum.photos/300/300?1",
            )
        )
    }
}