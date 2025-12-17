package com.andone.memorip.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.andone.memorip.presentation.home.model.GroupItem
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripAlpha
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun ImageCard(
    item: GroupItem,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MemoripPadding.PaddingXXXSmall)
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
                        .background(MemoripTheme.colors.black.copy(alpha = MemoripAlpha.IMAGE_PLACEHOLDER)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            R.string.home_overflow_count,
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