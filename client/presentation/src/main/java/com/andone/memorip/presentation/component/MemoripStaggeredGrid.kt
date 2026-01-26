package com.andone.memorip.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.component.StaggeredGridDimens.OVERLAY_HEIGHT
import com.andone.memorip.presentation.component.StaggeredGridDimens.STAGGERED_GRID_IMAGE_CORNER_RADIUS
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.R

private object StaggeredGridDimens {
    val STAGGERED_GRID_MIN_CELL_WIDTH = 160.dp
    val STAGGERED_GRID_IMAGE_CORNER_RADIUS = 16.dp
    val OVERLAY_HEIGHT = 52.dp
}

@Composable
fun MemoripStaggeredGrid(
    places: List<Place>,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(StaggeredGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
        verticalItemSpacing = MemoripSpace.SpaceXXSmall,
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall),
        modifier = modifier.fillMaxSize(),
    ) {
        items(items = places) { place ->
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
fun StaggeredImageItem(
    imageUrl: String,
    aspectRatio: Float,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = STAGGERED_GRID_IMAGE_CORNER_RADIUS,
    contentDescription: String? = null,
    location: String? = null
) {
    Column(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onImageClick
            )
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = cornerRadius))
            .background(color = MemoripTheme.colors.gray)
    ) {
        MemoripImage(
            imageUrl = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = OVERLAY_HEIGHT)
                .background(color = MemoripTheme.colors.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = MemoripPadding.PaddingXSmall),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = contentDescription?.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.place_list_no_title),
                    style = MemoripTheme.typography.labelRegular10,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(height = MemoripPadding.PaddingXXSmall))
                PlaceLocationText(
                    address = location?.takeIf { it.isNotBlank() }
                        ?: stringResource(R.string.place_list_no_address_info)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MemoripStaggeredGridPreview() {
    MemoripTheme {
        MemoripStaggeredGrid(
            places = DummyData.places,
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