package com.andone.memorip.presentation.placelist

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.grouplist.component.AddFloatingActionButton
import com.andone.memorip.presentation.placelist.component.PlaceListTopBar
import com.andone.memorip.presentation.placelist.model.PlaceListAction
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.placelist.model.PlaceListUiState
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.paging.compose.collectAsLazyPagingItems
import com.andone.memorip.presentation.component.MemoripStaggeredGrid
import com.andone.memorip.presentation.placelist.model.ListPlaceItems
import com.andone.memorip.presentation.placelist.model.PagingPlaceItems
import com.andone.memorip.presentation.placelist.model.PlaceItems

@Composable
fun PlaceListScreen(
    onPlaceClick: (Int) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagingItems = viewModel.placesPagingFlow.collectAsLazyPagingItems()

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
        places = PagingPlaceItems(pagingItems),
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListScreenContents(
    state: PlaceListUiState,
    places: PlaceItems,
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
        Box(modifier = Modifier.padding(paddingValues = padding)) {
            MemoripStaggeredGrid(
                places = places,
                onImageClick = { onAction(PlaceListAction.OnPlaceClick(id = it)) },
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(connection = clearFocusOnScroll)
                    .pointerInput(key1 = Unit) { detectTapGestures { focusManager.clearFocus() } },
            )
        }
    }
}

@Preview
@Composable
private fun PlaceListScreenContentsPreview() {
    MemoripTheme {
        PlaceListScreenContents(
            state = PlaceListUiState(),
            places = ListPlaceItems(items = DummyData.places),
            onAction = {},
        )
    }
}