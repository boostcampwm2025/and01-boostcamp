package com.andone.memorip.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import com.andone.memorip.presentation.groupdetail.model.PlaceImageItem
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object StaggeredGridDimens {
    val STAGGERED_GRID_MIN_CELL_WIDTH = 160.dp
    val STAGGERED_GRID_IMAGE_CORNER_RADIUS = 4.dp
}

@Composable
fun MemoripStaggeredGrid(
    images: List<PlaceImageItem>,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(StaggeredGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
        verticalItemSpacing = MemoripSpace.SpaceXXSmall,
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall),
        modifier = modifier.fillMaxSize(),
        content = {
            items(
                items = images,
                key = { it.id }
            ) { image ->
                StaggeredImageItem(
                    imageUrl = image.url,
                    aspectRatio = image.aspectRatio
                )
            }
        }
    )
}

@Composable
private fun StaggeredImageItem(
    imageUrl: String,
    aspectRatio: Float,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StaggeredGridDimens.STAGGERED_GRID_IMAGE_CORNER_RADIUS,
    contentDescription: String? = null
) {
    val description = contentDescription ?: stringResource(R.string.staggered_grid_image_content_description)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clip(RoundedCornerShape(cornerRadius))
            .background(MemoripTheme.colors.gray),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxWidth(),
            placeholder = ColorPainter(MemoripTheme.colors.offWhite),
            error = ColorPainter(MemoripTheme.colors.offWhite),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoripStaggeredGridPreview() {
    MemoripTheme {
        MemoripStaggeredGrid(images = DummyData.dummyImages)
    }
}

@Preview
@Composable
private fun StaggeredImageItemPreview() {
    MemoripTheme {
        StaggeredImageItem(
            imageUrl = "https://picsum.photos/id/1/200/300",
            aspectRatio = 1f
        )
    }
}