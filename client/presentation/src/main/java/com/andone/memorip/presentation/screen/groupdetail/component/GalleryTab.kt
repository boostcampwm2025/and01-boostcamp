package com.andone.memorip.presentation.screen.groupdetail.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.EmptyText
import com.andone.memorip.presentation.component.MemoripPagingList
import com.andone.memorip.presentation.component.StaggeredImageItem
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripPadding

private object StaggeredGridDimens {
    val STAGGERED_GRID_MIN_CELL_WIDTH = 160.dp
}

@Composable
fun GalleryTab(
    placesPagingItems: LazyPagingItems<Place>,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    MemoripPagingList(
        pagingItems = placesPagingItems,
        itemKey = { it.id },
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = MemoripPadding.PaddingXSmall),
        staggeredCells = StaggeredGridCells.Adaptive(minSize = StaggeredGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
        emptyContent = {
            EmptyText(
                text = stringResource(R.string.place_list_empty),
                modifier = Modifier.fillMaxSize()
            )
        },
        itemContent = { place ->
            val image = place.thumbnailImage
            StaggeredImageItem(
                imageUrl = image.url,
                aspectRatio = image.aspectRatio,
                onImageClick = { onImageClick(place.id) },
                contentDescription = place.name,
                location = place.address,
            )
        }
    )
}