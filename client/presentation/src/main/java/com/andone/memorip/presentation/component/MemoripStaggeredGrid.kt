package com.andone.memorip.presentation.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.component.StaggeredGridDimens.OVERLAY_HEIGHT
import com.andone.memorip.presentation.component.StaggeredGridDimens.OVERLAY_WIDTH
import com.andone.memorip.presentation.component.StaggeredGridDimens.STAGGERED_GRID_IMAGE_CORNER_RADIUS
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripAlpha
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object StaggeredGridDimens {
    val STAGGERED_GRID_MIN_CELL_WIDTH = 160.dp
    val STAGGERED_GRID_IMAGE_CORNER_RADIUS = 16.dp
    val OVERLAY_WIDTH = 104.dp
    val OVERLAY_HEIGHT = 40.dp
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
        items(places) { place ->
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
    var isExpanded by remember { mutableStateOf(false) }

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
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .then(
                    if (isExpanded) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier.width(width = OVERLAY_WIDTH)
                    }
                )
                .height(height = OVERLAY_HEIGHT)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = MemoripAlpha.IMAGE_OVERLAY),
                    shape = if (!isExpanded) {
                        RoundedCornerShape(topEnd = STAGGERED_GRID_IMAGE_CORNER_RADIUS)
                    } else {
                        RectangleShape
                    }
                )
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = { isExpanded = !isExpanded }
                ),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = MemoripPadding.PaddingXSmall),
                verticalArrangement = Arrangement.Center
            ) {
                if (contentDescription != null) {
                    Text(
                        text = contentDescription,
                        style = MemoripTheme.typography.label1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (!location.isNullOrBlank()) {
                    PlaceLocationText(address = location)
                }
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