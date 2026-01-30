package com.andone.memorip.presentation.screen.tripdetail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.andone.memorip.navigation.TripDetail
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.map.MapClusterManager
import com.andone.memorip.presentation.component.map.rememberBitmapMarkerLoader
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.tripdetail.component.TripDetailTopBar
import com.andone.memorip.presentation.screen.tripdetail.component.MapTab
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailAction
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailEvent
import com.andone.memorip.presentation.screen.tripdetail.model.MapBottomSheetStep
import com.andone.memorip.presentation.screen.placelist.PlaceListGrid
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.andone.memorip.presentation.util.DummyData
import com.naver.maps.geometry.LatLng

@Composable
fun TripDetailScreen(
    route: TripDetail,
    onBackClick: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TripDetailViewModel = hiltViewModel<TripDetailViewModel, TripDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(route) }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val placesPagingItems = viewModel.placesPagingFlow.collectAsLazyPagingItems()
    val clusteredItems by viewModel.clusteredItemsStateFlow.collectAsStateWithLifecycle()
    var places by remember { mutableStateOf<List<Place>>(emptyList()) }

    LaunchedEffect(placesPagingItems) {
        snapshotFlow { placesPagingItems.itemSnapshotList.items }
            .collect { items ->
                places = items
            }
    }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            TripDetailEvent.NavigateBack -> {
                onBackClick()
            }

            is TripDetailEvent.NavigateToPlaceDetail -> {
                onImageClick(event.id)
            }
        }
    }

    TripDetailScreenContent(
        tripName = uiState.tripName,
        currentPage = uiState.currentTab,
        places = places,
        placesPagingItems = placesPagingItems,
        clusteredItems = clusteredItems,
        mapSelectedPlace = uiState.mapSelectedPlace,
        mapBottomSheetContent = uiState.mapBottomSheetContent,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun TripDetailScreenContent(
    tripName: String,
    currentPage: Int,
    places: List<Place>,
    placesPagingItems: LazyPagingItems<Place>,
    clusteredItems: List<MapClusterManager.ClusterItem>,
    mapSelectedPlace: Place?,
    mapBottomSheetContent: MapBottomSheetStep,
    onAction: (TripDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        R.drawable.ic_image to R.string.trip_detail_tab_gallery,
        R.drawable.ic_map to R.string.trip_detail_tab_map
    )

    var mapLoaded by rememberSaveable { mutableStateOf(false) }
    val markerImages = rememberBitmapMarkerLoader(
        imageUrls = places.map { it.thumbnailImage.url }
    )

    Scaffold(
        topBar = {
            TripDetailTopBar(
                title = tripName,
                onBackClick = { onAction(TripDetailAction.OnBackClick) },
                onMenuClick = { onAction(TripDetailAction.OnMenuClick) },
                onSearchClick = { onAction(TripDetailAction.OnSearchClick) }
            )
        },
        contentWindowInsets = WindowInsets.navigationBars,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            SecondaryTabRow(
                selectedTabIndex = currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MemoripTheme.colors.background,
                contentColor = MemoripTheme.colors.onSurface
            ) {
                tabs.forEachIndexed { index, (iconRes, _) ->
                    Tab(
                        selected = currentPage == index,
                        onClick = { onAction(TripDetailAction.OnTabClick(currentTab = index)) },
                        icon = {
                            Icon(
                                painter = painterResource(iconRes),
                                contentDescription = stringResource(tabs[index].second),
                                tint = if (currentPage == index) {
                                    MemoripTheme.colors.primary
                                } else {
                                    MemoripTheme.colors.gray
                                }
                            )
                        }
                    )
                }
            }

            when (currentPage) {
                0 -> PlaceListGrid(
                    placePagingItems = placesPagingItems,
                    onPlaceClick = { id -> onAction(TripDetailAction.OnPlaceClick(id = id)) },
                    onRefresh = { /* TripDetail에서는 refresh 불필요 */ },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = MemoripPadding.AppHorizontalPadding,
                            bottom = MemoripPadding.AppHorizontalPadding
                        )
                )

                1 -> MapTab(
                    places = places,
                    clusteredItems = clusteredItems,
                    markerImages = markerImages,
                    mapBottomSheetContent = mapBottomSheetContent,
                    mapLoaded = mapLoaded,
                    onMapLoaded = { mapLoaded = true },
                    onAction = onAction,
                    modifier = Modifier.fillMaxSize(),
                    mapSelectedPlace = mapSelectedPlace
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TripDetailScreenContentPreview() {
    MemoripTheme {
        val clusteredItems = DummyData.places.map { place ->
            MapClusterManager.ClusterItem(
                position = LatLng(place.latitude, place.longitude),
                places = listOf(
                    MapClusterManager.PlaceClusterData(
                        id = place.placeId,
                        position = LatLng(place.latitude, place.longitude),
                        imageUrl = place.thumbnailImage.url,
                        placeData = place
                    )
                )
            )
        }

        TripDetailScreenContent(
            tripName = "그룹그룹그룹그룹그룹그룹그룹그룹그룹그룹",
            currentPage = 0,
            places = DummyData.places,
            placesPagingItems = DummyData.getPlacePagingItems(),
            clusteredItems = clusteredItems,
            mapSelectedPlace = null,
            mapBottomSheetContent = MapBottomSheetStep.PlaceList,
            onAction = {}
        )
    }
}
