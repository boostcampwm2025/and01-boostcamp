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
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.FullScreenErrorView
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.compose.rememberUpdatedMarkerState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapTab(
    places: List<Place>,
    markerImages: Map<String, Bitmap>,
    modifier: Modifier = Modifier
) {
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    // 모든 마커가 보이도록 카메라 위치 조정
    AdjustCameraToPlaces(
        places = places,
        cameraPositionState = cameraPositionState
    )

    Box(modifier = modifier) {
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
            }
        ) {
            PlaceMarkers(
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

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun PlaceMarkers(
    places: List<Place>,
    markerImages: Map<String, Bitmap>,
    onMarkerClick: (Place) -> Unit
) {
    places.forEach { place ->
        val imageUrl = place.thumbnailImage.url
        val bitmap = markerImages[imageUrl]

        if (bitmap != null) {
            MarkerComposable(
                keys = arrayOf(place.id, imageUrl),
                state = rememberUpdatedMarkerState(
                    position = LatLng(place.latitude, place.longitude)
                ),
                onClick = {
                    onMarkerClick(place)
                    true
                }
            ) {
                ImageMarker(imageBitmap = bitmap)
            }
        }
    }
}

private fun calculateBounds(places: List<Place>): LatLngBounds {
    val minLat = places.minOf { it.latitude }
    val maxLat = places.maxOf { it.latitude }
    val minLng = places.minOf { it.longitude }
    val maxLng = places.maxOf { it.longitude }

    return LatLngBounds(
        LatLng(minLat, minLng),
        LatLng(maxLat, maxLng)
    )
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
        FullScreenErrorView(
            title = "지도를 불러올 수 없습니다",
            message = "지도 인증에 실패했습니다\nNaver Cloud Platform 설정을 확인해주세요",
            onRetry = {}
        )
    }
}