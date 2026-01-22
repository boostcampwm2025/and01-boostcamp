package com.andone.memorip.presentation.component.map

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.theme.MemoripTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.rememberCameraPositionState

private object ReadOnlyMapViewConstants {
    const val DEFAULT_ZOOM_LEVEL = 15.0
    val DEFAULT_MARKER_WIDTH = 24.dp
    val DEFAULT_MARKER_HEIGHT = 32.dp
    const val DEFAULT_LATITUDE = 37.5666805
    const val DEFAULT_LONGITUDE = 126.9784147
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun ReadOnlyMapView(
    modifier: Modifier = Modifier,
    location: LocationUiModel? = null,
    zoomLevel: Double = ReadOnlyMapViewConstants.DEFAULT_ZOOM_LEVEL,
    properties: MapProperties = MemoripMapDefaults.defaultProperties,
    uiSettings: MapUiSettings = MemoripMapDefaults.readOnlyUiSettings,
    markerWidth: Dp = ReadOnlyMapViewConstants.DEFAULT_MARKER_WIDTH,
    markerHeight: Dp = ReadOnlyMapViewConstants.DEFAULT_MARKER_HEIGHT,
    onClick: (() -> Unit)? = null
) {
    val displayLocation = location ?: LocationUiModel(
        latitude = ReadOnlyMapViewConstants.DEFAULT_LATITUDE,
        longitude = ReadOnlyMapViewConstants.DEFAULT_LONGITUDE
    )

    val showMarker = location != null

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(
            LatLng(displayLocation.latitude, displayLocation.longitude),
            zoomLevel
        )
    }

    val markerState = remember(displayLocation) {
        MarkerState(position = LatLng(displayLocation.latitude, displayLocation.longitude))
    }

    MemoripNaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        onMapClick = onClick?.let { { _, _ -> it() } } ?: { _, _ -> }
    ) {
        if (showMarker) {
            Marker(
                state = markerState,
                width = markerWidth,
                height = markerHeight
            )
        }
    }
}

@Preview(name = "ReadOnlyMapView", showBackground = true)
@Composable
private fun ReadOnlyMapViewWithMarkerPreview() {
    MemoripTheme {
        ReadOnlyMapView(
            modifier = Modifier.fillMaxWidth(),
            location = LocationUiModel(
                name = "서울시청",
                address = "서울특별시 중구 세종대로 110",
                latitude = 37.5666805,
                longitude = 126.9784147
            )
        )
    }
}