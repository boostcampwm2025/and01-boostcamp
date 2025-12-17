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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

private object StaggeredGridDimens {
    val STAGGERED_GRID_MIN_CELL_WIDTH = 160.dp
    val STAGGERED_GRID_IMAGE_CORNER_RADIUS = 4.dp
}

@Composable
fun MemoripStaggeredGrid(modifier: Modifier = Modifier) {
    val images = remember { generateRandomImageUrls(50) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(StaggeredGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
        verticalItemSpacing = MemoripSpace.SpaceXXSmall,
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall),
        modifier = modifier.fillMaxSize(),
        content = {
            items(images) { image ->
                StaggeredImageItem(
                    imageUrl = image.url
                )
            }
        }
    )
}

@Composable
private fun StaggeredImageItem(
    imageUrl: String,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StaggeredGridDimens.STAGGERED_GRID_IMAGE_CORNER_RADIUS,
    contentDescription: String = stringResource(R.string.place_image)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(cornerRadius))
            .background(MemoripTheme.colors.gray),
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

// todo: 실제 Image 모델로 대체 에정
data class ImageItem(
    val id: Int,
    val url: String
)

/**
 * 랜덤 높이 이미지 count 개 생성.
 * picsum 사이트에서 200x랜덤height로 crop해서 가져옴.
 * todo: 백엔드에서 받아온 이미지로 변경
 */
fun generateRandomImageUrls(count: Int): List<ImageItem> {
    return (1..count).map { id ->
        val height = (50..400).random()

        ImageItem(
            id = id,
            url = "https://picsum.photos/id/$id/200/$height"
        )
    }
}