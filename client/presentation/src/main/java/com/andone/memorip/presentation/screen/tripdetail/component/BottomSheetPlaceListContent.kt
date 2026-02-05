package com.andone.memorip.presentation.screen.tripdetail.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailAction
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

@Composable
fun BottomSheetPlaceListContent(
    placesPagingItems: LazyPagingItems<Place>,
    listState: LazyListState,
    onAction: (TripDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(
            horizontal = MemoripPadding.PaddingMedium,
            vertical = MemoripPadding.PaddingXSmall
        )
    ) {
        items(
            count = placesPagingItems.itemCount,
            key = { index -> placesPagingItems.peek(index)?.placeId ?: index }
        ) { index ->
            placesPagingItems[index]?.let { place ->
                BottomSheetPlaceListItem(
                    place = place,
                    onClick = {
                        onAction(TripDetailAction.OnPlaceClick(place))
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetPlaceListContentPreview() {
    MemoripTheme {
        val listState = rememberLazyListState()
        BottomSheetPlaceListContent(
            placesPagingItems = DummyData.getPlacePagingItems(),
            listState = listState,
            onAction = {}
        )
    }
}
