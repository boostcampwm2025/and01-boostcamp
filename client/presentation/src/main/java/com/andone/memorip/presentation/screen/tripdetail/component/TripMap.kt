package com.andone.memorip.presentation.screen.tripdetail.component

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.andone.memorip.presentation.component.map.MapClusterManager
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.tripdetail.model.MapBottomSheetStep
import com.andone.memorip.presentation.screen.tripdetail.model.PlaceViewMode
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailAction
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.paging.compose.LazyPagingItems
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object TripMapConstant {
    const val BOUND_PADDING = 200
    const val CAMERA_ANIMATION_DURATION = 500
    const val MICRO_ANIMATION_DURATION = 1
    const val MICRO_ZOOM_DELTA = 0.0001
    const val CAMERA_UPDATE_DELAY = 100L
    const val INITIAL_MAX_ZOOM = 10
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun TripMap(
    places: List<Place>,
    placesPagingItems: LazyPagingItems<Place>,
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
    viewMode: PlaceViewMode = PlaceViewMode.LIST,
    hasMorePages: Boolean = false,
    onBottomSheetExpandedChange: (Boolean) -> Unit = {}
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val cameraPositionState: CameraPositionState = rememberCameraPositionState()
    var savedPlaceListSheetValue by remember { mutableStateOf<SheetValue?>(null) }
    var isCameraInitialized by remember { mutableStateOf(false) }

    val isBottomSheetExpanded by remember {
        derivedStateOf {
            try {
                scaffoldState.bottomSheetState.requireOffset() <= 20f
            } catch (e: IllegalStateException) {
                false
            }
        }
    }

    LaunchedEffect(isBottomSheetExpanded) {
        onBottomSheetExpandedChange(isBottomSheetExpanded)
    }

    LaunchedEffect(Unit) {
        scaffoldState.bottomSheetState.partialExpand()
    }

    LaunchedEffect(places, cameraPositionState) {
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
            }.distinctUntilChanged()
        ) { currentPlaces, cameraData ->
            Pair(currentPlaces, cameraData)
        }
            .collect { (currentPlaces, cameraData) ->
                if (currentPlaces.isNotEmpty() && !isCameraInitialized) {
                    val bounds = LatLngBounds.Builder().apply {
                        currentPlaces.forEach { place ->
                            include(LatLng(place.latitude, place.longitude))
                        }
                    }.build()

                    val cameraUpdate = CameraUpdate.fitBounds(bounds, TripMapConstant.BOUND_PADDING)
                    cameraPositionState.move(cameraUpdate)
                    scope.launch {
                        delay(TripMapConstant.CAMERA_UPDATE_DELAY)
                        val currentZoom = cameraPositionState.position.zoom
                        if (currentZoom > TripMapConstant.INITIAL_MAX_ZOOM) {
                            cameraPositionState.animate(
                                update = CameraUpdate.zoomTo(TripMapConstant.INITIAL_MAX_ZOOM.toDouble()),
                                durationMs = TripMapConstant.CAMERA_ANIMATION_DURATION
                            )
                        } else {
                            val microUpdate = CameraUpdate.zoomBy(TripMapConstant.MICRO_ZOOM_DELTA)
                            cameraPositionState.animate(
                                update = microUpdate,
                                durationMs = TripMapConstant.MICRO_ANIMATION_DURATION
                            )
                        }
                    }
                    onAction(TripDetailAction.OnMapPlacesUpdate(currentPlaces))

                    isCameraInitialized = true
                    onAction(TripDetailAction.OnMapInitialized)
                }

                if (isCameraInitialized && cameraData != null) {
                    val (projection, zoom) = cameraData
                    onAction(TripDetailAction.OnMapCameraChange(projection, zoom))
                }
            }
    }

    LaunchedEffect(mapSelectedPlace) {
        var lastSelectedPlaceId: String? = null
        snapshotFlow { mapSelectedPlace }
            .collect { selectedPlace ->
                if (selectedPlace != null && selectedPlace.placeId != lastSelectedPlaceId) {
                    lastSelectedPlaceId = selectedPlace.placeId
                    scope.launch {
                        val cameraUpdate = CameraUpdate.scrollTo(
                            LatLng(selectedPlace.latitude, selectedPlace.longitude)
                        )
                        cameraPositionState.animate(
                            update = cameraUpdate,
                            durationMs = TripMapConstant.CAMERA_ANIMATION_DURATION
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

    TripMapContent(
        places = places,
        placesPagingItems = placesPagingItems,
        clusteredItems = clusteredItems,
        markerImages = markerImages,
        cameraPositionState = cameraPositionState,
        scaffoldState = scaffoldState,
        isBottomSheetExpanded = isBottomSheetExpanded,
        mapLoaded = mapLoaded,
        mapBottomSheetContent = mapBottomSheetContent,
        mapSelectedPlace = mapSelectedPlace,
        tripName = tripName,
        startDate = startDate,
        endDate = endDate,
        viewMode = viewMode,
        hasMorePages = hasMorePages,
        onAction = onAction,
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
                    durationMs = TripMapConstant.CAMERA_ANIMATION_DURATION
                )
            }
        },
        onMapLoaded = onMapLoaded,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Preview(showBackground = true)
@Composable
private fun TripMapPreview() {
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

        TripMap(
            places = DummyData.places,
            placesPagingItems = DummyData.getPlacePagingItems(),
            clusteredItems = clusteredItems,
            markerImages = emptyMap(),
            mapBottomSheetContent = MapBottomSheetStep.PlaceList,
            mapLoaded = true,
            onMapLoaded = {},
            onAction = {},
            viewMode = PlaceViewMode.LIST,
            modifier = Modifier.fillMaxSize()
        )
    }
}
