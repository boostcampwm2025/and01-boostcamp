package com.andone.memorip.presentation.placecreate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.placecreate.component.LocationMapPreviewConstant.CAMERA_POSITION_ZOOM
import com.andone.memorip.presentation.placecreate.component.LocationMapPreviewDimen.markerHeight
import com.andone.memorip.presentation.placecreate.component.LocationMapPreviewDimen.markerWidth
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

private object LocationMapPreviewDimen {
    val markerWidth = 24.dp
    val markerHeight = 32.dp
}

private object LocationMapPreviewConstant {
    const val CAMERA_POSITION_ZOOM = 16.0
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun LocationMapPreview(
    location: LocationUiModel?,
    onLocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(shape = MemoripTheme.shapes.roundedSmall)
            .clickable(onClick = onLocationClick)
            .background(color = MemoripTheme.colors.gray)
    ) {
        location?.let {
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition(LatLng(it.latitude, it.longitude), CAMERA_POSITION_ZOOM)
            }
            NaverMap(
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    isScrollGesturesEnabled = false,
                    isZoomGesturesEnabled = false,
                    isTiltGesturesEnabled = false,
                    isRotateGesturesEnabled = false,
                    isZoomControlEnabled = false,
                    isScaleBarEnabled = false,
                    isLogoClickEnabled = false,
                    isCompassEnabled = false
                ),
                onMapClick = { _, _ -> onLocationClick() }
            ) {
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    width = markerWidth,
                    height = markerHeight
                )
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxWidth())
        }
    }
}