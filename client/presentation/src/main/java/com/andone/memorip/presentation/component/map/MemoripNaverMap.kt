package com.andone.memorip.presentation.component.map

import android.graphics.PointF
import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.map.MoemoripNaverMapDimen.BLOCK_LOGO_HEIGHT
import com.andone.memorip.presentation.component.map.MoemoripNaverMapDimen.BLOCK_LOGO_WIDTH
import com.andone.memorip.presentation.theme.MemoripTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationSource
import com.naver.maps.map.Symbol
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.NaverMapComposable
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.indoor.IndoorSelection
import java.util.Locale

private object MoemoripNaverMapDimen {
    val BLOCK_LOGO_WIDTH = 80.dp
    val BLOCK_LOGO_HEIGHT = 40.dp
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MemoripNaverMap(
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier,
    properties: MapProperties? = null,
    uiSettings: MapUiSettings? = null,
    locationSource: LocationSource? = null,
    locale: Locale? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onMapClick: (PointF, LatLng) -> Unit = { _, _ -> },
    onMapLongClick: (PointF, LatLng) -> Unit = { _, _ -> },
    onMapDoubleTab: (PointF, LatLng) -> Boolean = { _, _ -> false },
    onMapTwoFingerTap: (PointF, LatLng) -> Boolean = { _, _ -> false },
    onMapLoaded: () -> Unit = {},
    onLocationChange: (Location) -> Unit = {},
    onOptionChange: () -> Unit = {},
    onSymbolClick: (Symbol) -> Boolean = { false },
    onIndoorSelectionChange: (IndoorSelection?) -> Unit = {},
    content:
    @Composable @NaverMapComposable
        () -> Unit = {}
) {
    var hasLoaded by remember { mutableStateOf(false) }

    val wrappedOnMapLoaded = {
        hasLoaded = true
        onMapLoaded()
    }

    Box {
        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = properties ?: MemoripMapDefaults.defaultProperties,
            uiSettings = uiSettings ?: MemoripMapDefaults.defaultUiSettings,
            locationSource = locationSource,
            locale = locale,
            onMapClick = onMapClick,
            onMapLongClick = onMapLongClick,
            onMapDoubleTab = onMapDoubleTab,
            onMapTwoFingerTap = onMapTwoFingerTap,
            onMapLoaded = wrappedOnMapLoaded,
            onLocationChange = onLocationChange,
            onOptionChange = onOptionChange,
            onSymbolClick = onSymbolClick,
            onIndoorSelectionChange = onIndoorSelectionChange,
            contentPadding = contentPadding,
            content = content
        )

        if (!hasLoaded) {
            LoadingIndicatorScreen()
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = contentPadding.calculateBottomPadding())
                .size(width = BLOCK_LOGO_WIDTH, height = BLOCK_LOGO_HEIGHT)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
        )
    }
}

@Preview(name = "MemoripNaverMap", showBackground = true)
@Composable
private fun MemoripNaverMapPreview() {
    MemoripTheme {
        MemoripNaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition(LatLng(37.5666805, 126.9784147), 11.0)
            }
        )
    }
}