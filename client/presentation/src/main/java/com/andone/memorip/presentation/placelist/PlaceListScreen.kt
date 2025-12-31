package com.andone.memorip.presentation.placelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.grouplist.component.AddFloatingActionButton
import com.andone.memorip.presentation.grouplist.model.GroupListAction
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.placelist.component.PhotoItem
import com.andone.memorip.presentation.placelist.component.PlaceListTopBar
import com.andone.memorip.presentation.placelist.model.PlaceListAction
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.placelist.model.PlaceListUiState
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

private object StaggeredGridDimens {
    val STAGGERED_GRID_MIN_CELL_WIDTH = 160.dp
}


@Composable
fun PlaceListScreen(
    onPlaceClick: (Int) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            is PlaceListEvent.NavigateToPlaceCreate -> {
                onCreatePlaceClick()
            }

            is PlaceListEvent.NavigatePlaceDetail -> {
                onPlaceClick(event.id)
            }

            PlaceListEvent.ShowSnackBar -> TODO()
        }
    }

    PlaceListScreenContents(
        state = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListScreenContents(
    state: PlaceListUiState,
    onAction: (PlaceListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            PlaceListTopBar(
                scrollBehavior = scrollBehavior,
                query = state.query,
                onQueryChange = { onAction(PlaceListAction.OnQueryChange(query = it)) }
            )
        },
        floatingActionButton = { AddFloatingActionButton(onClick = { onAction(PlaceListAction.OnFABClick) }) },
    ) { padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = StaggeredGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding,
            horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall),
            verticalItemSpacing = MemoripSpace.SpaceXXSmall
        ) {
            items(items = state.places) { place ->
                PhotoItem(place = place)
            }
        }
    }
}

@Preview
@Composable
private fun PlaceListScreenContentsPreview() {
    MemoripTheme {
        PlaceListScreenContents(
            state = PlaceListUiState().copy(places = DummyData.places),
            onAction = {},
        )
    }
}