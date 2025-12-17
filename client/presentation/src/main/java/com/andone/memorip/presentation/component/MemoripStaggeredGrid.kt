package com.andone.memorip.presentation.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.model.ImageItem
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
                StaggeredImageItem(imageUrl = image.url)
            }
        }
    )
}

@Composable
private fun StaggeredImageItem(
    imageUrl: String,
    modifier: Modifier = Modifier,
    fixedHeight: Dp? = null,
    cornerRadius: Dp = StaggeredGridDimens.STAGGERED_GRID_IMAGE_CORNER_RADIUS,
    contentDescription: String = stringResource(R.string.staggered_grid_image_content_description)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (fixedHeight != null) {
                    Modifier.height(fixedHeight)
                } else {
                    Modifier.wrapContentHeight()
                }
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(MemoripTheme.colors.gray),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxWidth(),
            placeholder = ColorPainter(MemoripTheme.colors.gray),
            error = ColorPainter(MemoripTheme.colors.gray),
            contentScale = ContentScale.FillWidth
        )
    }
}

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

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MemoripStaggeredGridPreview() {
    MemoripTheme {
        // 30개의 랜덤한 높이 생성 (50~400)
        val dummyHeights = remember {
            List(30) { (50..400).random() }
        }
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(StaggeredGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
            verticalItemSpacing = MemoripSpace.SpaceXXSmall,
            horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(30) { index ->
                StaggeredImageItem(
                    imageUrl = "https://picsum.photos/id/${index + 1}/200/${dummyHeights[index]}",
                    fixedHeight = dummyHeights[index].dp
                )
            }
        }
    }
}