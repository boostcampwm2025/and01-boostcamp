package com.andone.memorip.presentation.screen.placelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.EmptyText
import com.andone.memorip.presentation.component.MemoripPagingList
import com.andone.memorip.presentation.screen.placelist.component.StaggeredImageItem
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.screen.placelist.MemoripMotion.AnimationDuration
import com.andone.memorip.presentation.screen.placelist.MemoripMotion.ScrollThreshold
import com.andone.memorip.presentation.screen.placelist.component.PlaceListTopBar
import com.andone.memorip.presentation.screen.placelist.component.RegionFilter
import com.andone.memorip.presentation.screen.placelist.component.RegionSelectBottomSheet
import com.andone.memorip.presentation.screen.placelist.component.TagFilter
import com.andone.memorip.presentation.screen.placelist.model.PlaceListAction
import com.andone.memorip.presentation.screen.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.screen.placelist.model.PlaceListUiState
import com.andone.memorip.presentation.screen.placelist.model.SelectedRegionState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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

            is PlaceListEvent.NavigateToPlaceDetail -> {
                onPlaceClick(event.id)
            }

            PlaceListEvent.ShowSnackBar -> {

            }

            PlaceListEvent.RefreshPagingData -> {
                placesPagingItems.refresh()
            }
        }
    }

    PlaceListScreenContent(
        state = uiState,
        placePagingItems = placesPagingItems,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListScreenContent(
    state: PlaceListUiState,
    placePagingItems: LazyPagingItems<Place>,
    onAction: (PlaceListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var showRegionBottomSheet by remember { mutableStateOf(value = false) }

    var isFilterVisible by remember { mutableStateOf(value = true) }
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

    if (showRegionBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRegionBottomSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MemoripTheme.colors.background,
            contentColor = MemoripTheme.colors.onSurface
        ) {
            RegionSelectBottomSheet(
                currentRegionList = state.currentRegionList,
                selectedRegionState = state.selectedRegionState,
                onConfirmClick = { showRegionBottomSheet = false },
                onRegionChipClick = { onAction(PlaceListAction.OnRegionChipClick(region = it)) }
            )
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
        contentWindowInsets = WindowInsets()
    ) { padding ->
        Column(modifier = Modifier.padding(paddingValues = padding)) {
            AnimatedVisibility(
                visible = isFilterVisible,
                enter = expandVertically(animationSpec = tween(durationMillis = AnimationDuration)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(durationMillis = AnimationDuration)) + fadeOut()
            ) {
                Column {
                    FilterSection(
                        onChangeRegionClick = { showRegionBottomSheet = true },
                        onAddTagClick = {},
                        modifier = Modifier.padding(horizontal = MemoripPadding.AppHorizontalPadding),
                        tags = DummyData.categories.toImmutableList(),
                        selectedRegionState = state.selectedRegionState
                    )
                    Spacer(modifier = Modifier.padding(vertical = MemoripPadding.PaddingXSmall))
                }
            }

            PlaceListGrid(
                placePagingItems = placePagingItems,
                onPlaceClick = { id -> onAction(PlaceListAction.OnPlaceClick(id = id)) },
                onRefresh = { onAction(PlaceListAction.OnRefreshPull) },
                modifier = Modifier.fillMaxSize(),
                clearFocusOnScroll = clearFocusOnScroll,
                focusManager = focusManager
            )
        }
    }
}

@Composable
private fun FilterSection(
    onChangeRegionClick: () -> Unit,
    onAddTagClick: () -> Unit,
    modifier: Modifier = Modifier,
    tags: ImmutableList<TagUiModel> = persistentListOf(),
    selectedRegionState: SelectedRegionState = SelectedRegionState(),
) {
    Column(modifier = modifier) {
        RegionFilter(
            modifier = Modifier.clickable(onClick = onChangeRegionClick),
            selectedRegionState = selectedRegionState,
        )
        TagFilter(
            tags = tags,
            onAddTagClick = onAddTagClick
        )
    }
}

@Composable
fun PlaceListGrid(
    placePagingItems: LazyPagingItems<Place>,
    onPlaceClick: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    clearFocusOnScroll: NestedScrollConnection? = null,
    focusManager: FocusManager? = null
) {
    val isRefreshing =
        placePagingItems.loadState.refresh is LoadState.Loading && placePagingItems.itemCount > 0

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        MemoripPagingList(
            pagingItems = placePagingItems,
            itemKey = { it.id },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = MemoripPadding.AppHorizontalPadding)
                .then(
                    if (clearFocusOnScroll != null && focusManager != null) {
                        Modifier
                            .nestedScroll(connection = clearFocusOnScroll)
                            .pointerInput(key1 = Unit) { detectTapGestures { focusManager.clearFocus() } }
                    } else {
                        Modifier
                    }
                ),
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
                    onImageClick = { onPlaceClick(place.id) },
                    contentDescription = place.name,
                    location = place.address,
                )
            }
        )
    }
}

@Preview
@Composable
private fun PlaceListScreenContentsPreview() {
    MemoripTheme {
        PlaceListScreenContent(
            state = PlaceListUiState(),
            placePagingItems = DummyData.getPlacePagingItems(),
            onAction = {},
        )
    }
}