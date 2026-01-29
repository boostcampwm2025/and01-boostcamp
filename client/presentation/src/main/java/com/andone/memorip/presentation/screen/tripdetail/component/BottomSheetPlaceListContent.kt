package com.andone.memorip.presentation.screen.tripdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailAction
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

@Composable
fun BottomSheetPlaceListContent(
    places: List<Place>,
    listState: LazyListState,
    onAction: (TripDetailAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.trip_detail_place_list_title),
            style = MemoripTheme.typography.headlineBold20,
            color = MemoripTheme.colors.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MemoripPadding.PaddingMedium,
                    vertical = MemoripPadding.PaddingXSmall
                )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall),
            contentPadding = PaddingValues(
                horizontal = MemoripPadding.PaddingMedium,
                vertical = MemoripPadding.PaddingXSmall
            )
        ) {
            items(
                items = places,
                key = { it.placeId }
            ) { place ->
                BottomSheetPlaceListItem(
                    place = place,
                    onClick = {
                        onAction(TripDetailAction.OnMapPlaceClick(place))
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
            places = DummyData.places,
            listState = listState,
            onAction = {}
        )
    }
}
