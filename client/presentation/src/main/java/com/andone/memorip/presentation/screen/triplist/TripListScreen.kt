package com.andone.memorip.presentation.screen.triplist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.component.TripView
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.screen.triplist.component.TripListTopBar
import com.andone.memorip.presentation.screen.triplist.model.TripListAction
import com.andone.memorip.presentation.screen.triplist.model.TripListEvent
import com.andone.memorip.presentation.model.TripUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

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
            onAction = viewModel::onAction,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreenContent(
    trips: List<TripUiModel>,
    onAction: (TripListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TripListTopBar(onSearchClick = { }) },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = MemoripPadding.AppHorizontalPadding,
                top = innerPadding.calculateTopPadding(),
                end = MemoripPadding.AppHorizontalPadding,
                bottom = innerPadding.calculateBottomPadding()
            ),
            verticalArrangement = Arrangement.spacedBy(space = MemoripPadding.PaddingXSmall)
        ) {
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
}

@Composable
@Preview(showBackground = true)
private fun TripListScreenContentsPreview() {
    MemoripTheme {
        TripListScreenContent(
            trips = DummyData.trips,
            onAction = {}
        )
    }
}
