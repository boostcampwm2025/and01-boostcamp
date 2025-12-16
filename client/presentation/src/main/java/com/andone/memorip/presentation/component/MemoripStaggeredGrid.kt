package com.andone.memorip.presentation.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripDimen
import com.andone.memorip.presentation.theme.MemoripTheme

data class ImageItem(
    val id: Int,
    val url: String
)

@Composable
fun MemoripStaggeredGrid(
    modifier: Modifier = Modifier,
) {
    val randomSizedPhotos = remember { 
        generateRandomImageUrls(50) 
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(MemoripDimen.staggeredGridMinCellWidth),
        verticalItemSpacing = MemoripDimen.staggeredGridSpacing,
        horizontalArrangement = Arrangement.spacedBy(MemoripDimen.staggeredGridSpacing),
        modifier = modifier.fillMaxSize(),
        content = {
            items(randomSizedPhotos) { image ->
                StaggeredImageItem(
                    imageUrl = image.url,
                    modifier = modifier
                )
            }
        }
    )
}

@Composable
private fun StaggeredImageItem(
    imageUrl: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = MemoripDimen.imageCornerRadius,
    contentDescription: String = stringResource(R.string.place_image)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(MemoripTheme.colors.gray)
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth,
            placeholder = ColorPainter(MemoripTheme.colors.gray),
            error = ColorPainter(MemoripTheme.colors.gray),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * 랜덤 높이 이미지 count 개 생성.
 * todo: 백엔드에서 받아온 이미지로 변경
 */
fun generateRandomImageUrls(count: Int): List<ImageItem> {
    return (1..count).map { id ->
        val height = (200..400).random()

        ImageItem(
            id = id,
            url = "https://picsum.photos/id/$id/200/$height"
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MemoripStaggeredGridPreview() {
    MemoripTheme {
        MemoripStaggeredGrid()
    }
}