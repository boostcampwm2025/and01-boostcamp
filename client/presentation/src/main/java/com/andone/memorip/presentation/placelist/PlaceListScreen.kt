package com.andone.memorip.presentation.placelist

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.grouplist.component.AddFloatingActionButton
import com.andone.memorip.presentation.placelist.component.PhotoItem
import com.andone.memorip.presentation.placelist.component.PlaceListTopBar
import com.andone.memorip.presentation.placelist.model.PlaceListAction
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.placelist.model.PlaceListUiState
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.pointerInput


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
    val focusManager = LocalFocusManager.current

    val clearFocusOnScroll = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                focusManager.clearFocus()
                return Offset.Zero
            }
        }
    }

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
        contentWindowInsets = WindowInsets(),
    ) { padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = StaggeredGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(clearFocusOnScroll)
                .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } },
            contentPadding = padding,
            horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall),
            verticalItemSpacing = MemoripSpace.SpaceXXSmall
        ) {
            items(items = state.places) { place ->
                PhotoItem(
                    place = place,
                    onClick = { onAction(PlaceListAction.OnPlaceClick(id=it)) },
                )
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