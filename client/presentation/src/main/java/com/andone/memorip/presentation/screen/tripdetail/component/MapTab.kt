package com.andone.memorip.presentation.screen.tripdetail.component

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.map.InteractiveMultiMarkerMapView
import com.andone.memorip.presentation.component.map.MapClusterManager
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.tripdetail.model.PlaceViewMode
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailAction
import com.andone.memorip.presentation.screen.tripdetail.model.MapBottomSheetStep
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripShadow
import com.andone.memorip.presentation.theme.MemoripSpace
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.theme.MemoripTheme
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.padding
import com.andone.memorip.presentation.util.DummyData
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private object MapTabDimen {
    val SHEET_PEEK_HEIGHT = 200.dp
}

private object MapTabConstant {
    const val BOUND_PADDING = 200
    const val CAMERA_ANIMATION_DURATION = 500
    const val MICRO_ANIMATION_DURATION = 1
    const val MICRO_ZOOM_DELTA = 0.0001
    const val CAMERA_UPDATE_DELAY = 100L
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun MapTab(
    places: List<Place>,
    clusteredItems: List<MapClusterManager.ClusterItem>,
    markerImages: Map<String, Bitmap>,
    mapBottomSheetContent: MapBottomSheetStep,
    mapLoaded: Boolean,
    onMapLoaded: () -> Unit,
    onAction: (TripDetailAction) -> Unit,
    modifier: Modifier = Modifier,
    mapSelectedPlace: Place? = null,
    tripName: String? = null,
    startDate: String? = null,
    endDate: String? = null,
    viewMode: PlaceViewMode = PlaceViewMode.LIST
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val cameraPositionState: CameraPositionState = rememberCameraPositionState()
    var savedPlaceListSheetValue by remember { mutableStateOf<SheetValue?>(null) }

    LaunchedEffect(Unit) {
        scaffoldState.bottomSheetState.partialExpand()
    }

    LaunchedEffect(places, cameraPositionState, mapSelectedPlace) {
        var isCameraInitialized = false
        var lastSelectedPlaceId: String? = null

        combine(
            snapshotFlow { places },
            snapshotFlow {
                val isMoving = cameraPositionState.isMoving
                val projection = cameraPositionState.projection
                val zoom = cameraPositionState.position.zoom

                if (isMoving) {
                    null
                } else if (projection != null) {
                    Pair(projection, zoom)
                } else {
                    null
                }
            }.distinctUntilChanged(),
            snapshotFlow { mapSelectedPlace }
        ) { currentPlaces, cameraData, selectedPlace ->
            Triple(currentPlaces, cameraData, selectedPlace)
        }
            .collect { (currentPlaces, cameraData, selectedPlace) ->
                if (currentPlaces.isNotEmpty() && !isCameraInitialized) {
                    val bounds = LatLngBounds.Builder().apply {
                        currentPlaces.forEach { place ->
                            include(LatLng(place.latitude, place.longitude))
                        }
                    }.build()

                    val cameraUpdate = CameraUpdate.fitBounds(bounds, MapTabConstant.BOUND_PADDING)
                    cameraPositionState.move(cameraUpdate)
                    scope.launch {
                        delay(MapTabConstant.CAMERA_UPDATE_DELAY)
                        val microUpdate = CameraUpdate.zoomBy(MapTabConstant.MICRO_ZOOM_DELTA)
                        cameraPositionState.animate(
                            update = microUpdate,
                            durationMs = MapTabConstant.MICRO_ANIMATION_DURATION
                        )
                    }
                    onAction(TripDetailAction.OnMapPlacesUpdate(currentPlaces))

                    isCameraInitialized = true
                    onAction(TripDetailAction.OnMapInitialized)
                }

                if (isCameraInitialized && cameraData != null) {
                    val (projection, zoom) = cameraData
                    onAction(TripDetailAction.OnMapCameraChange(projection, zoom))
                }

                if (selectedPlace != null && selectedPlace.placeId != lastSelectedPlaceId) {
                    lastSelectedPlaceId = selectedPlace.placeId
                    scope.launch {
                        val cameraUpdate = CameraUpdate.scrollTo(
                            LatLng(selectedPlace.latitude, selectedPlace.longitude)
                        )
                        cameraPositionState.animate(
                            update = cameraUpdate,
                            durationMs = MapTabConstant.CAMERA_ANIMATION_DURATION
                        )
                    }
                } else if (selectedPlace == null) {
                    lastSelectedPlaceId = null
                }
            }
    }

    LaunchedEffect(mapBottomSheetContent) {
        when (mapBottomSheetContent) {
            MapBottomSheetStep.PlaceList -> {
                when (savedPlaceListSheetValue) {
                    SheetValue.Expanded -> scaffoldState.bottomSheetState.expand()
                    else -> scaffoldState.bottomSheetState.partialExpand()
                }
            }

            MapBottomSheetStep.PlaceDetail -> {
                savedPlaceListSheetValue = scaffoldState.bottomSheetState.currentValue
                scaffoldState.bottomSheetState.partialExpand()
            }
        }
    }

    BackHandler(enabled = mapBottomSheetContent == MapBottomSheetStep.PlaceDetail) {
        onAction(TripDetailAction.OnPlaceDetailBottomSheetClose)
    }

    Box(modifier = modifier) {
        if (!mapLoaded) {
            LoadingIndicatorScreen(modifier = Modifier.fillMaxSize())
        }

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetContent = {
                MapBottomSheetContent(
                    bottomSheetContent = mapBottomSheetContent,
                    places = places,
                    selectedPlace = mapSelectedPlace,
                    viewMode = viewMode,
                    onAction = onAction
                )
            },
            sheetPeekHeight = MapTabDimen.SHEET_PEEK_HEIGHT,
            sheetContainerColor = MemoripTheme.colors.background,
            sheetDragHandle = null,
            modifier = Modifier.fillMaxSize()
        ) {
            InteractiveMultiMarkerMapView(
                initialBounds = places.map { LatLng(it.latitude, it.longitude) },
                onMapLoaded = onMapLoaded,
                contentPadding = PaddingValues(bottom = MapTabDimen.SHEET_PEEK_HEIGHT),
                cameraPositionState = cameraPositionState,
                modifier = Modifier.fillMaxSize()
            ) {
                if (mapLoaded) {
                    PlaceImageMarkers(
                        clusteredItems = clusteredItems,
                        markerImages = markerImages,
                        onMarkerClick = { place ->
                            onAction(TripDetailAction.OnPlaceClick(place = place))
                        },
                        onClusterClick = { clusterItem ->
                            scope.launch {
                                val cameraUpdate = CameraUpdate
                                    .scrollAndZoomTo(
                                        clusterItem.position,
                                        cameraPositionState.position.zoom + 1
                                    )
                                cameraPositionState.animate(
                                    update = cameraUpdate,
                                    durationMs = MapTabConstant.CAMERA_ANIMATION_DURATION
                                )
                            }
                        }
                    )
                }
            }
        }

        if (startDate != null && endDate != null && tripName != null) {
            TripScheduleCard(
                tripName = tripName,
                startDate = startDate,
                endDate = endDate,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = MemoripPadding.PaddingMedium,
                        start = MemoripPadding.PaddingMedium,
                        end = MemoripPadding.PaddingMedium
                    )
            )
        }
    }
}

@Composable
private fun MapBottomSheetContent(
    bottomSheetContent: MapBottomSheetStep,
    places: List<Place>,
    selectedPlace: Place?,
    viewMode: PlaceViewMode,
    onAction: (TripDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val placeListState = rememberLazyListState()

    Surface(
        tonalElevation = MemoripShadow.Large,
        shadowElevation = MemoripShadow.Large,
        color = MemoripTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MemoripPadding.PaddingSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(MemoripPadding.PaddingXXXLarge)
                    .height(MemoripSpace.SpaceXXSmall)
                    .clip(RoundedCornerShape(MemoripLineWidth.Small))
                    .background(MemoripTheme.colors.gray1)
            )
            Spacer(modifier = Modifier.height(MemoripPadding.PaddingXSmall))
            when (bottomSheetContent) {
                MapBottomSheetStep.PlaceList -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        PlaceListHeader(
                            placeCount = places.size,
                            viewMode = viewMode,
                            onViewModeToggle = { onAction(TripDetailAction.OnViewModeToggle) }
                        )

                        when (viewMode) {
                            PlaceViewMode.LIST -> {
                                BottomSheetPlaceListContent(
                                    places = places,
                                    listState = placeListState,
                                    onAction = onAction
                                )
                            }

                            PlaceViewMode.GRID -> {
                                BottomSheetPlaceGridContent(
                                    places = places,
                                    onAction = onAction
                                )
                            }
                        }
                    }
                }

                MapBottomSheetStep.PlaceDetail -> {
                    selectedPlace?.let { place ->
                        BottomSheetPlaceDetailContent(
                            place = place,
                            onCloseClick = { onAction(TripDetailAction.OnPlaceDetailBottomSheetClose) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MapTabPreview() {
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
                ),
                isCluster = false
            )
        }

        MapTab(
            places = DummyData.places,
            clusteredItems = clusteredItems,
            markerImages = emptyMap(),
            mapSelectedPlace = null,
            mapBottomSheetContent = MapBottomSheetStep.PlaceList,
            onAction = {},
            mapLoaded = true,
            onMapLoaded = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
