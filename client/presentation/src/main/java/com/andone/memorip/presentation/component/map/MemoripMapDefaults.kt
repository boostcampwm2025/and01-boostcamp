package com.andone.memorip.presentation.component.map

import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings

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