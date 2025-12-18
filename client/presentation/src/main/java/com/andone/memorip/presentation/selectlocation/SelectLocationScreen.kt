package com.andone.memorip.presentation.selectlocation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.selectlocation.SelectLocationScreenConstants.CAMERA_ANIMATION_DURATION
import com.andone.memorip.presentation.selectlocation.SelectLocationScreenDimens.markerHeight
import com.andone.memorip.presentation.selectlocation.SelectLocationScreenDimens.markerWidth
import com.andone.memorip.presentation.theme.LocalMemoripTypography
import com.andone.memorip.presentation.theme.MemoripTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

private object SelectLocationScreenDimens {
    val markerWidth = 24.dp
    val markerHeight = 32.dp
}

private object SelectLocationScreenConstants {
    const val CAMERA_ANIMATION_DURATION = 1000L
}

@Composable
fun SelectLocationScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    SelectLocationContent(
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun SelectLocationContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = rememberCameraPositionState()
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    Box(modifier = modifier) {
        SelectLocationTopBar(
            onBackClick = onBackClick,
            modifier = Modifier.zIndex(1f)
        )

        NaverMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = remember {
                MapUiSettings(
                    isCompassEnabled = true,
                    isZoomControlEnabled = true,
                    isLocationButtonEnabled = true
                )
            },
            onMapClick = { _, latLng ->
                selectedLocation = latLng
                cameraPositionState.move(
                    update = CameraUpdate
                        .scrollTo(latLng)
                        .animate(CameraAnimation.Fly, CAMERA_ANIMATION_DURATION)
                )
            }
        ) {
            selectedLocation?.let { location ->
                Marker(
                    state = MarkerState(position = location),
                    width = markerWidth,
                    height = markerHeight,
                    onClick = {
                        selectedLocation = null
                        true
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectLocationTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.select_location_title),
                style = LocalMemoripTypography.current.headline2
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.select_group_back_button_description)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MemoripTheme.colors.offWhite,
            navigationIconContentColor = MemoripTheme.colors.black,
            titleContentColor = MemoripTheme.colors.black
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun SelectLocationScreenPreview() {
    MemoripTheme {
        SelectLocationScreen(onBackClick = {})
    }
}