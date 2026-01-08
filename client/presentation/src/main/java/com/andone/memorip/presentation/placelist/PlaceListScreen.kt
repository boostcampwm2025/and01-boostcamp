package com.andone.memorip.presentation.placelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.EmptyText
import com.andone.memorip.presentation.component.MemoripPagingList
import com.andone.memorip.presentation.component.StaggeredImageItem
import com.andone.memorip.presentation.grouplist.component.AddFloatingActionButton
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.placelist.MemoripMotion.AnimationDuration
import com.andone.memorip.presentation.placelist.MemoripMotion.ScrollThreshold
import com.andone.memorip.presentation.placelist.component.FilterSection
import com.andone.memorip.presentation.placelist.component.PlaceListTopBar
import com.andone.memorip.presentation.placelist.model.PlaceListAction
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.placelist.model.PlaceListUiState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import kotlinx.collections.immutable.toImmutableList

private object StaggeredGridDimens {
    val STAGGERED_GRID_MIN_CELL_WIDTH = 160.dp
}

private object MemoripMotion {
    const val ScrollThreshold = 10
    const val AnimationDuration = 300
}

@Composable
fun PlaceListScreen(
    onPlaceClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val placesPagingItems = viewModel.placesPagingFlow.collectAsLazyPagingItems()

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
        places = placesPagingItems,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListScreenContents(
    state: PlaceListUiState,
    places: LazyPagingItems<Place>,
    onAction: (PlaceListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val focusManager = LocalFocusManager.current

    var isFilterVisible by remember { mutableStateOf(true) }

    val clearFocusOnScroll = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                focusManager.clearFocus()

                val delta = available.y
                if (delta < -ScrollThreshold) {
                    isFilterVisible = false
                } else if (delta > ScrollThreshold) {
                    isFilterVisible = true
                }

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
    ) { innerPadding ->
        Column(modifier = Modifier.padding(paddingValues = innerPadding)) {
            AnimatedVisibility(
                visible = isFilterVisible,
                enter = expandVertically(animationSpec = tween(durationMillis = AnimationDuration)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(durationMillis = AnimationDuration)) + fadeOut()
            ) {
                Column {
                    FilterSection(
                        onChangeRegionClick = {},
                        onAddTagClick = {},
                        modifier = Modifier.padding(horizontal = MemoripPadding.PaddingMedium),
                        tags = DummyData.categories.toImmutableList()
                    )
                    Spacer(modifier = Modifier.padding(vertical = MemoripPadding.PaddingXSmall))
                }
            }

            MemoripPagingList(
                pagingItems = places,
                itemKey = { it.id },
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(connection = clearFocusOnScroll)
                    .pointerInput(key1 = Unit) { detectTapGestures { focusManager.clearFocus() } },
                staggeredCells = StaggeredGridCells.Adaptive(StaggeredGridDimens.STAGGERED_GRID_MIN_CELL_WIDTH),
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
                        onImageClick = { onAction(PlaceListAction.OnPlaceClick(id = place.id)) }
                    )
                }
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
            places = DummyData.getPlacePagingItems(),
            onAction = {},
        )
    }
}