package com.andone.memorip.presentation.component.map

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.res.stringResource
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripShadow
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
    onNavigateToExternalMap: ((LocationUiModel) -> Unit)? = null,
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

    Box(
        modifier = modifier
    ) {
        MemoripNaverMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState,
            properties = properties,
            uiSettings = uiSettings,
        ) {
            if (showMarker) {
                Marker(
                    state = markerState,
                    width = markerWidth,
                    height = markerHeight
                )
            }
        }
        if (location != null && onNavigateToExternalMap != null) {
            MapExternalViewButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = MemoripPadding.PaddingSmall,
                        bottom = MemoripPadding.PaddingSmall
                    ),
                onClick = { onNavigateToExternalMap(location) }
            )
        }
    }
}

@Composable
private fun MapExternalViewButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = MemoripPadding.PaddingXSmall,
        vertical = MemoripPadding.PaddingXXSmall
    ),
) {
    val shape = MemoripTheme.shapes.roundedXXSmall

    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick),
        shape = shape,
        color = MemoripTheme.colors.background,
        shadowElevation = MemoripShadow.Medium
    ) {
        Row(
            modifier = Modifier
                .background(Color.Transparent)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
        ) {
            Text(
                text = stringResource(id = R.string.place_detail_view_in_map_app),
                style = MemoripTheme.typography.bodyBold12,
                color = MemoripTheme.colors.primary
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_launch),
                contentDescription = null,
                modifier = Modifier.size(MemoripIconSize.IconSizeXXSmall),
                tint = MemoripTheme.colors.primary
            )
        }
    }
}

@Preview(showBackground = true)
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

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MapExternalViewButtonPreview() {
    MemoripTheme {
        MapExternalViewButton(
            onClick = {}
        )
    }
}
