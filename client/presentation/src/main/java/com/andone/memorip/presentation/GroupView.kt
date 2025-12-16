package com.andone.memorip.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.domain.group.buildBento5x3Items
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun GroupView(
    name: String,
    images: List<String>,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(name)
        GroupLayout(
            items = buildBento5x3Items(images),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
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
            name = "테스트 그룹",
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