package com.andone.memorip.presentation.screen.groupdetail.component

import android.graphics.Bitmap
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.map.InteractiveMultiMarkerMapView
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailAction
import com.andone.memorip.presentation.screen.groupdetail.model.MapBottomSheetStep
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
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
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun MapTab(
    places: List<Place>,
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
    val cameraPositionState = rememberCameraPositionState()

    var savedPlaceListSheetValue by remember { mutableStateOf<SheetValue?>(null) }

    LaunchedEffect(Unit) {
        scaffoldState.bottomSheetState.partialExpand()
    }

    LaunchedEffect(mapSelectedPlace) {
        if (mapSelectedPlace != null) {
            scope.launch {
                val cameraUpdate = CameraUpdate.scrollTo(
                    LatLng(mapSelectedPlace.latitude, mapSelectedPlace.longitude)
                )
                cameraPositionState.animate(
                    update = cameraUpdate,
                    durationMs = MapTabConstant.CAMERA_ANIMATION_DURATION
                )
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
            LaunchedEffect(places) {
                if (places.isNotEmpty()) {
                    val bounds = LatLngBounds.Builder().apply {
                        places.forEach { place ->
                            include(LatLng(place.latitude, place.longitude))
                        }
                    }.build()
                    val cameraUpdate = CameraUpdate.fitBounds(bounds, MapTabConstant.BOUND_PADDING)
                    cameraPositionState.move(cameraUpdate)
                }
            }

            InteractiveMultiMarkerMapView(
                initialBounds = places.map { LatLng(it.latitude, it.longitude) },
                onMapLoaded = onMapLoaded,
                contentPadding = PaddingValues(bottom = MapTabDimen.SHEET_PEEK_HEIGHT),
                cameraPositionState = cameraPositionState,
                modifier = Modifier.fillMaxSize()
            ) {
                if (mapLoaded) {
                    PlaceImageMarkers(
                        places = places,
                        markerImages = markerImages,
                        onMarkerClick = { place -> onAction(GroupDetailAction.OnMapPlaceClick(place = place)) }
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
    onAction: (GroupDetailAction) -> Unit
) {
    Surface(
        tonalElevation = MapTabDimen.SHEET_TONAL_ELEVATION,
        shadowElevation = MapTabDimen.SHEET_SHADOW_ELEVATION,
        color = MemoripTheme.colors.background
    ) {
        when (bottomSheetContent) {
            MapBottomSheetStep.PlaceList -> {
                BottomSheetPlaceListContent(
                    places = places,
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
        MapTab(
            places = DummyData.places,
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