package com.andone.memorip.presentation.groupdetail.component

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.ErrorFullScreen
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.calculateBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapTab(
    places: List<Place>,
    markerImages: Map<String, Bitmap>,
    modifier: Modifier = Modifier
) {
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    val cameraPositionState = rememberCameraPositionState()
    var mapLoadError by remember { mutableStateOf(false) }
    var mapLoaded by remember { mutableStateOf(false) }

    // todo: 맵 화면에 들어올 때 map을 로딩하는게 아니라 갤러리에 들어왔을 때 로딩하는 것으로 변경 고민.
    LaunchedEffect(Unit) {
        delay(2000)
        if (!mapLoaded && !mapLoadError) {
            mapLoadError = true
        }
    }

    // 모든 마커가 보이도록 카메라 위치 조정
    AdjustCameraToPlaces(
        places = places,
        cameraPositionState = cameraPositionState
    )

    Box(modifier = modifier) {
        if (mapLoadError) {
            ErrorFullScreen(
                onRetry = {
                    mapLoadError = false
                    mapLoaded = false
                },
                message = stringResource(R.string.errorfullscreen_error_map_message)
            )
        } else {
            NaverMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = remember {
                    MapProperties(
                        maxZoom = 20.0,
                        minZoom = 5.0,
                        locationTrackingMode = LocationTrackingMode.NoFollow
                    )
                },
                uiSettings = remember {
                    MapUiSettings(
                        isLocationButtonEnabled = true,
                        isZoomControlEnabled = true
                    )
                },
                onMapLoaded = { mapLoaded = true },
                onMapClick = { _, _ -> selectedPlace = null }
            ) {
                PlaceImageMarkers(
                    places = places,
                    markerImages = markerImages,
                    onMarkerClick = { selectedPlace = it }
                )
            }
            selectedPlace?.let { place ->
                PlaceImagesBottomSheet(
                    placeName = place.name,
                    images = place.images,
                    onDismiss = { selectedPlace = null }
                )
            }
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun AdjustCameraToPlaces(
    places: List<Place>,
    cameraPositionState: CameraPositionState
) {
    LaunchedEffect(places) {
        if (places.isNotEmpty()) {
            val bounds = calculateBounds(places)
            val cameraUpdate = CameraUpdate.fitBounds(bounds, 100)
            cameraPositionState.move(cameraUpdate)
        }
    }
}

@Preview(name = "Map Tab - Normal", showBackground = true)
@Composable
private fun MapTabPreview() {
    MemoripTheme {
        MapTab(
            places = DummyData.places,
            markerImages = emptyMap(),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(name = "Map Tab - Error", showBackground = true)
@Composable
private fun MapTabErrorPreview() {
    MemoripTheme {
        ErrorFullScreen(
            onRetry = {},
        )
    }
}