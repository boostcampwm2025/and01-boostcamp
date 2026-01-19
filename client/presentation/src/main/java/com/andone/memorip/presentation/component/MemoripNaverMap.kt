package com.andone.memorip.presentation.component

import android.graphics.PointF
import android.location.Location
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationSource
import com.naver.maps.map.Symbol
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapComposable
import com.naver.maps.map.indoor.IndoorSelection
import java.util.Locale

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MemoripNaverMap(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    properties: MapProperties? = null,
    uiSettings: MapUiSettings? = null,
    locationSource: LocationSource? = null,
    locale: Locale? = null,
    onMapClick: (PointF, LatLng) -> Unit = { _, _ -> },
    onMapLongClick: (PointF, LatLng) -> Unit = { _, _ -> },
    onMapDoubleTab: (PointF, LatLng) -> Boolean = { _, _ -> false },
    onMapTwoFingerTap: (PointF, LatLng) -> Boolean = { _, _ -> false },
    onMapLoaded: () -> Unit = {},
    onLocationChange: (Location) -> Unit = {},
    onOptionChange: () -> Unit = {},
    onSymbolClick: (Symbol) -> Boolean = { false },
    onIndoorSelectionChange: (IndoorSelection?) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content:
    @Composable @NaverMapComposable
        () -> Unit = {}
) {
    NaverMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties ?: MemoripMapDefaults.defaultProperties,
        uiSettings = uiSettings ?: MemoripMapDefaults.defaultUiSettings,
        locationSource = locationSource,
        locale = locale,
        onMapClick = onMapClick,
        onMapLongClick = onMapLongClick,
        onMapDoubleTab = onMapDoubleTab,
        onMapTwoFingerTap = onMapTwoFingerTap,
        onMapLoaded = onMapLoaded,
        onLocationChange = onLocationChange,
        onOptionChange = onOptionChange,
        onSymbolClick = onSymbolClick,
        onIndoorSelectionChange = onIndoorSelectionChange,
        contentPadding = contentPadding,
        content = content
    )
}

object MemoripMapDefaults {
    val defaultProperties: MapProperties = MapProperties(
        maxZoom = 20.0,
        minZoom = 5.0,
        locationTrackingMode = LocationTrackingMode.NoFollow
    )

    val defaultUiSettings: MapUiSettings = MapUiSettings(
        isScrollGesturesEnabled = true,
        isZoomGesturesEnabled = true,
        isTiltGesturesEnabled = true,
        isRotateGesturesEnabled = true,
        isLogoClickEnabled = false
    )

    val readOnlyUiSettings: MapUiSettings = MapUiSettings(
        isScrollGesturesEnabled = false,
        isZoomGesturesEnabled = false,
        isTiltGesturesEnabled = false,
        isRotateGesturesEnabled = false,
        isCompassEnabled = false,
        isScaleBarEnabled = false,
        isZoomControlEnabled = false,
        isIndoorLevelPickerEnabled = false,
        isLocationButtonEnabled = false,
        isLogoClickEnabled = false
    )

    val interactiveUiSettings: MapUiSettings = MapUiSettings(
        isScrollGesturesEnabled = true,
        isZoomGesturesEnabled = true,
        isTiltGesturesEnabled = true,
        isRotateGesturesEnabled = true,
        isCompassEnabled = false,
        isScaleBarEnabled = false,
        isZoomControlEnabled = true,
        isIndoorLevelPickerEnabled = true,
        isLocationButtonEnabled = true,
        isLogoClickEnabled = false
    )
}