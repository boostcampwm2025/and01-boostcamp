package com.andone.memorip.presentation.screen.groupdetail.component

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.map.InteractiveMultiMarkerMapView
import com.andone.memorip.presentation.component.map.MapClusterManager
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailAction
import com.andone.memorip.presentation.screen.groupdetail.model.MapBottomSheetStep
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

private object MapTabDimen {
    val SHEET_PEEK_HEIGHT = 200.dp
    val SHEET_TONAL_ELEVATION = 8.dp
    val SHEET_SHADOW_ELEVATION = 8.dp
}

private object MapTabConstant {
    const val BOUND_PADDING = 200
    const val CAMERA_ANIMATION_DURATION = 500
    const val MICRO_ANIMATION_DURATION = 1
    const val MICRO_ZOOM_DELTA = 0.0001
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
    onAction: (GroupDetailAction) -> Unit,
    modifier: Modifier = Modifier,
    mapSelectedPlace: Place? = null
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
                    val microUpdate = CameraUpdate.zoomBy(MapTabConstant.MICRO_ZOOM_DELTA)
                    cameraPositionState.animate(
                        update = microUpdate,
                        durationMs = MapTabConstant.MICRO_ANIMATION_DURATION
                    )
                    onAction(GroupDetailAction.OnMapPlacesUpdate(currentPlaces))

                    isCameraInitialized = true
                    onAction(GroupDetailAction.OnMapInitialized)
                }

                if (isCameraInitialized && cameraData != null) {
                    val (projection, zoom) = cameraData
                    onAction(GroupDetailAction.OnMapCameraChange(projection, zoom))
                }

                if (selectedPlace != null && selectedPlace.id != lastSelectedPlaceId) {
                    lastSelectedPlaceId = selectedPlace.id
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
        onAction(GroupDetailAction.OnMapPlaceClose)
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
                            onAction(GroupDetailAction.OnMapPlaceClick(place = place))
                        },
                        onClusterClick = { clusterItem ->
                            scope.launch {
                                val cameraUpdate = CameraUpdate
                                    .scrollAndZoomTo(clusterItem.position, cameraPositionState.position.zoom + 1)
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
    }
}

@Composable
private fun MapBottomSheetContent(
    bottomSheetContent: MapBottomSheetStep,
    places: List<Place>,
    selectedPlace: Place?,
    onAction: (GroupDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val placeListState = rememberLazyListState()

    Surface(
        tonalElevation = MapTabDimen.SHEET_TONAL_ELEVATION,
        shadowElevation = MapTabDimen.SHEET_SHADOW_ELEVATION,
        color = MemoripTheme.colors.background
    ) {
        when (bottomSheetContent) {
            MapBottomSheetStep.PlaceList -> {
                BottomSheetPlaceListContent(
                    places = places,
                    listState = placeListState,
                    onAction = onAction
                )
            }
            MapBottomSheetStep.PlaceDetail -> {
                selectedPlace?.let { place ->
                    BottomSheetPlaceDetailContent(
                        place = place,
                        onCloseClick = { onAction(GroupDetailAction.OnMapPlaceClose) }
                    )
                }
            }
        }
    }
}

@Preview(name = "지도탭 프리뷰", showBackground = true)
@Composable
private fun MapTabPreview() {
    MemoripTheme {
        val clusteredItems = DummyData.places.map { place ->
            MapClusterManager.ClusterItem(
                position = LatLng(place.latitude, place.longitude),
                places = listOf(
                    MapClusterManager.PlaceClusterData(
                        id = place.id,
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
