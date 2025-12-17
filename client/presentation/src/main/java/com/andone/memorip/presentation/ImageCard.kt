package com.andone.memorip.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.andone.memorip.domain.group.GroupItem
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun ImageCard(item: GroupItem) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .clip(memoripShapes.defaultCorner)
                .background(MemoripTheme.colors.offWhite),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            if (item.overNumber > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MemoripTheme.colors.black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.home_overflow_count,
                            item.overNumber
                        ),
                        color = MemoripTheme.colors.white
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ImageCardPreview() {
    MemoripTheme {
        ImageCard(
            item = GroupItem(
                colSpan = 4,
                rowSpan = 3,
            )
        )
    }
}