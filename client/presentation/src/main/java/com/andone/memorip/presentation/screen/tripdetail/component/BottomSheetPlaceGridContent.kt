package com.andone.memorip.presentation.screen.tripdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.placelist.component.StaggeredImageItem
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailAction
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object BottomSheetPlaceGridDimens {
    val STAGGERED_GRID_MIN_CELL_WIDTH = 160.dp
}

@Composable
fun BottomSheetPlaceGridContent(
    places: List<Place>,
    onAction: (TripDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gridState = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = BottomSheetPlaceGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MemoripPadding.PaddingMedium),
        state = gridState,
        verticalItemSpacing = MemoripSpace.SpaceXXSmall,
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall),
        contentPadding = PaddingValues(
            horizontal = MemoripPadding.PaddingXSmall,
            vertical = MemoripPadding.PaddingXSmall
        )
    ) {
        items(
            items = places,
            key = { it.placeId }
        ) { place ->
            val image = place.thumbnailImage
            StaggeredImageItem(
                imageUrl = image.url,
                aspectRatio = image.aspectRatio,
                onImageClick = { onAction(TripDetailAction.OnPlaceClick(place)) },
                contentDescription = place.name,
                location = place.address,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetPlaceGridContentPreview() {
    MemoripTheme {
        BottomSheetPlaceGridContent(
            places = DummyData.places,
            onAction = {}
        )
    }
}
