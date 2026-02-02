package com.andone.memorip.presentation.screen.tripdetail

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.andone.memorip.navigation.TripDetail
import com.andone.memorip.presentation.component.map.MapClusterManager
import com.andone.memorip.presentation.component.map.rememberBitmapMarkerLoader
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.TripUiModel
import com.andone.memorip.presentation.screen.tripdetail.component.TripDetailTopBar
import com.andone.memorip.presentation.screen.tripdetail.component.MapTab
import com.andone.memorip.presentation.screen.tripdetail.model.PlaceViewMode
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailAction
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailEvent
import com.andone.memorip.presentation.screen.tripdetail.model.MapBottomSheetStep
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.andone.memorip.presentation.util.DummyData
import com.naver.maps.geometry.LatLng

@Composable
fun TripDetailScreen(
    route: TripDetail,
    onBackClick: () -> Unit,
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
            TripDetailEvent.NavigateBack -> onBackClick()
        }
    }

    TripDetailScreenContent(
        tripName = uiState.tripName,
        places = places,
        clusteredItems = clusteredItems,
        mapSelectedPlace = uiState.mapSelectedPlace,
        mapBottomSheetContent = uiState.mapBottomSheetContent,
        tripInfo = uiState.tripInfo,
        viewMode = uiState.placeViewMode,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun TripDetailScreenContent(
    tripName: String,
    places: List<Place>,
    clusteredItems: List<MapClusterManager.ClusterItem>,
    mapSelectedPlace: Place?,
    mapBottomSheetContent: MapBottomSheetStep,
    tripInfo: TripUiModel?,
    viewMode: PlaceViewMode,
    onAction: (TripDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
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
        MapTab(
            places = places,
            clusteredItems = clusteredItems,
            markerImages = markerImages,
            mapBottomSheetContent = mapBottomSheetContent,
            mapLoaded = mapLoaded,
            onMapLoaded = { mapLoaded = true },
            onAction = onAction,
            mapSelectedPlace = mapSelectedPlace,
            tripName = tripInfo?.name,
            startDate = tripInfo?.startDate,
            endDate = tripInfo?.endDate,
            viewMode = viewMode,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        )
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
            tripName = "서울대공원 주암 나들이",
            places = DummyData.places,
            clusteredItems = clusteredItems,
            mapSelectedPlace = null,
            mapBottomSheetContent = MapBottomSheetStep.PlaceList,
            tripInfo = TripUiModel(
                id = "1",
                name = "서울대공원 주암 나들이",
                images = emptyList(),
                startDate = "2026-02-05",
                endDate = "2026-02-07"
            ),
            viewMode = PlaceViewMode.LIST,
            onAction = {}
        )
    }
}
