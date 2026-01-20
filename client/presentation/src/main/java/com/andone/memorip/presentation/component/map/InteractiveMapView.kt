package com.andone.memorip.presentation.component.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.component.map.InteractiveMapViewDimen.BoundsPadding
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
fun InteractiveMapView(
    initialBounds: List<LatLng>,
    modifier: Modifier = Modifier,
    properties: MapProperties? = null,
    uiSettings: MapUiSettings? = null,
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
        properties = properties ?: MemoripMapDefaults.defaultProperties,
        uiSettings = uiSettings ?: MemoripMapDefaults.interactiveUiSettings,
        contentPadding = contentPadding,
        onMapLoaded = onMapLoaded
    ) {
        content()
    }
}