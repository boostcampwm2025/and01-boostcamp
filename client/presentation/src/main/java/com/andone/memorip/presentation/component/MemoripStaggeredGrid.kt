package com.andone.memorip.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.placelist.model.ListPlaceItems
import com.andone.memorip.presentation.placelist.model.PlaceItems
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object StaggeredGridDimens {
    val STAGGERED_GRID_MIN_CELL_WIDTH = 160.dp
    val STAGGERED_GRID_IMAGE_CORNER_RADIUS = 4.dp
}

@Composable
fun MemoripStaggeredGrid(
    places: PlaceItems,
    onImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(StaggeredGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
        verticalItemSpacing = MemoripSpace.SpaceXXSmall,
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall),
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            count = places.itemCount,
            key = { index -> places[index]?.id ?: index }
        ) { index ->
            val place = places[index] ?: return@items

            val image = place.thumbnailImage

            StaggeredImageItem(
                imageUrl = image.url,
                aspectRatio = image.aspectRatio,
                onImageClick = { onImageClick(place.id) }
            )
        }
    }
}

@Composable
private fun StaggeredImageItem(
    imageUrl: String,
    aspectRatio: Float,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = StaggeredGridDimens.STAGGERED_GRID_IMAGE_CORNER_RADIUS,
    contentDescription: String? = null
) {
    Box(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onImageClick
            )
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clip(RoundedCornerShape(cornerRadius))
            .background(MemoripTheme.colors.gray),
        contentAlignment = Alignment.Center
    ) {
        MemoripImage(
            imageUrl = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoripStaggeredGridPreview() {
    MemoripTheme {
        MemoripStaggeredGrid(
            places = ListPlaceItems(items = DummyData.places),
            onImageClick = {}
        )
    }
}

@Preview
@Composable
private fun StaggeredImageItemPreview() {
    MemoripTheme {
        StaggeredImageItem(
            imageUrl = "https://picsum.photos/id/1/200/300",
            aspectRatio = 1f,
            onImageClick = {}
        )
    }
}