package com.andone.memorip.presentation.screen.triplist

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.EmptyText
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.TripView
import com.andone.memorip.presentation.component.dialog.MemoripInputDialog
import com.andone.memorip.presentation.screen.triplist.component.TripListStickyHeader
import com.andone.memorip.presentation.screen.triplist.component.TripListTopBar
import com.andone.memorip.presentation.screen.triplist.model.TripListAction
import com.andone.memorip.presentation.screen.triplist.model.TripListEvent
import com.andone.memorip.presentation.model.TripUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun TripListScreen(
    onTripClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TripListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            is TripListEvent.NavigateToTripDetail -> {
                onTripClick(event.tripId)
            }

            is TripListEvent.NavigateToPlaceCreate -> {
                onCreatePlaceClick()
            }
        }
    }

    if (uiState.isLoading) {
        LoadingIndicatorScreen()
    } else {
        TripListScreenContent(
            trips = uiState.trips,
            searchQuery = uiState.searchQuery,
            isStickyHeaderVisible = uiState.isStickyHeaderVisible,
            isCreateTripDialogVisible = uiState.isCreateTripDialogVisible,
            newTripName = uiState.newTripName,
            onAction = viewModel::onAction,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TripListScreenContent(
    trips: List<TripUiModel>,
    searchQuery: String,
    isStickyHeaderVisible: Boolean,
    isCreateTripDialogVisible: Boolean,
    newTripName: String,
    onAction: (TripListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        var previousIndex = listState.firstVisibleItemIndex
        var previousOffset = listState.firstVisibleItemScrollOffset

        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }
            .distinctUntilChanged()
            .collect { (currentIndex, currentOffset) ->
                val isScrollingDown = if (currentIndex != previousIndex) {
                    currentIndex > previousIndex
                } else {
                    currentOffset > previousOffset
                }
                onAction(TripListAction.OnScrollStateChange(isScrollingDown))

                previousIndex = currentIndex
                previousOffset = currentOffset
            }
    }

    Scaffold(
        modifier = modifier,
        topBar = { TripListTopBar() },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )

        if (trips.isEmpty()) {
            TripListEmptyContent(
                searchQuery = searchQuery,
                isStickyHeaderVisible = isStickyHeaderVisible,
                onAction = onAction,
                modifier = contentModifier
            )
        } else {
            TripListNotEmptyContent(
                trips = trips,
                searchQuery = searchQuery,
                isStickyHeaderVisible = isStickyHeaderVisible,
                listState = listState,
                onAction = onAction,
                modifier = contentModifier
            )
        }
        if (isCreateTripDialogVisible) {
            MemoripInputDialog(
                title = stringResource(R.string.select_trip_dialog_title),
                value = newTripName,
                onValueChange = { onAction(TripListAction.OnNewTripNameChange(it)) },
                onConfirmClick = { onAction(TripListAction.OnConfirmCreateTrip) },
                onCancelClick = { onAction(TripListAction.OnDismissCreateTripDialog) },
                onDismissRequest = { onAction(TripListAction.OnDismissCreateTripDialog) },
                hint = stringResource(R.string.select_trip_dialog_hint),
                label = stringResource(R.string.select_trip_dialog_label),
                maxLength = 20
            )
        }
    }
}

@Composable
private fun TripListEmptyContent(
    searchQuery: String,
    isStickyHeaderVisible: Boolean,
    onAction: (TripListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val emptyMessageRes = if (searchQuery.isEmpty()) {
        R.string.trip_list_empty
    } else {
        R.string.trip_list_search_empty
    }

    Column(
        modifier = modifier
            .padding(
                horizontal = MemoripPadding.AppHorizontalPadding,
            )
    ) {
        TripListStickyHeader(
            searchQuery = searchQuery,
            isStickyHeaderVisible = isStickyHeaderVisible,
            onCreateNewTripClick = { onAction(TripListAction.OnCreateNewTripClick) },
            onSearchQueryChange = { query ->
                onAction(
                    TripListAction.OnSearchQueryChange(
                        query
                    )
                )
            }
        )

        EmptyText(
            text = stringResource(id = emptyMessageRes),
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        )
    }
}

@Composable
private fun TripListNotEmptyContent(
    trips: List<TripUiModel>,
    searchQuery: String,
    isStickyHeaderVisible: Boolean,
    listState: LazyListState,
    onAction: (TripListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MemoripPadding.AppHorizontalPadding,
            vertical = MemoripPadding.PaddingSmall
        ),
        verticalArrangement = Arrangement.spacedBy(space = MemoripPadding.PaddingSmall)
    ) {
        stickyHeader {
            TripListStickyHeader(
                searchQuery = searchQuery,
                isStickyHeaderVisible = isStickyHeaderVisible,
                onCreateNewTripClick = { onAction(TripListAction.OnCreateNewTripClick) },
                onSearchQueryChange = { query ->
                    onAction(
                        TripListAction.OnSearchQueryChange(
                            query
                        )
                    )
                }
            )
        }

        items(items = trips) { trip ->
            TripView(
                name = trip.name,
                onTripClick = { onAction(TripListAction.OnTripClick(tripId = trip.id)) },
                onAddClick = { onAction(TripListAction.OnTripClick(tripId = trip.id)) },
                images = trip.images
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun TripListScreenContentPreview() {
    MemoripTheme {
        TripListScreenContent(
            trips = DummyData.trips,
            searchQuery = "",
            isStickyHeaderVisible = true,
            isCreateTripDialogVisible = false,
            newTripName = "",
            onAction = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun TripListScreenContentAllEmptyPreview() {
    MemoripTheme {
        TripListScreenContent(
            trips = emptyList(),
            searchQuery = "",
            isStickyHeaderVisible = true,
            isCreateTripDialogVisible = false,
            newTripName = "",
            onAction = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun TripListScreenContentEmptyPreview() {
    MemoripTheme {
        TripListScreenContent(
            trips = emptyList(),
            searchQuery = "ㅇㅇㅇ",
            isStickyHeaderVisible = true,
            isCreateTripDialogVisible = false,
            newTripName = "",
            onAction = {}
        )
    }
}
