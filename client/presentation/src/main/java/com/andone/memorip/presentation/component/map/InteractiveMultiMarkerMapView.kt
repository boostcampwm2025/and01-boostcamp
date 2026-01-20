package com.andone.memorip.presentation.component.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.component.map.InteractiveMapViewDimen.BoundsPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.rememberCameraPositionState

private object InteractiveMapViewDimen {
    const val BoundsPadding = 200
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun InteractiveMultiMarkerMapView(
    initialBounds: List<LatLng>,
    modifier: Modifier = Modifier,
    properties: MapProperties = MemoripMapDefaults.defaultProperties,
    uiSettings: MapUiSettings = MemoripMapDefaults.interactiveUiSettings,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onMapLoaded: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(initialBounds) {
        if (initialBounds.isNotEmpty()) {
            val bounds = LatLngBounds.Builder().apply {
                initialBounds.forEach { include(it) }
            }.build()
            val cameraUpdate = CameraUpdate.fitBounds(bounds, BoundsPadding)
            cameraPositionState.move(cameraUpdate)
        }
    }

    MemoripNaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        contentPadding = contentPadding,
        onMapLoaded = onMapLoaded
    ) {
        content()
    }
}

@Preview(name = "InteractiveMapView", showBackground = true)
@Composable
private fun InteractiveMapViewEmptyPreview() {
    MemoripTheme {
        InteractiveMultiMarkerMapView(
            initialBounds = listOf(LatLng(37.5666805, 126.9784147)),
            modifier = Modifier.fillMaxSize(),
            content = {}
        )
    }
}