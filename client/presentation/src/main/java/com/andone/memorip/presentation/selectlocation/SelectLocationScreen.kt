package com.andone.memorip.presentation.selectlocation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.selectlocation.SelectLocationScreenConstants.CAMERA_ANIMATION_DURATION
import com.andone.memorip.presentation.selectlocation.SelectLocationScreenDimens.markerHeight
import com.andone.memorip.presentation.selectlocation.SelectLocationScreenDimens.markerWidth
import com.andone.memorip.presentation.selectlocation.component.LocationSelectionButton
import com.andone.memorip.presentation.selectlocation.model.SelectLocationAction
import com.andone.memorip.presentation.selectlocation.model.SelectLocationEvent
import com.andone.memorip.presentation.theme.LocalMemoripTypography
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle
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
    modifier: Modifier = Modifier,
    viewModel: SelectLocationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            is SelectLocationEvent.SelectLocation -> {

            }

            SelectLocationEvent.NavigateBack -> onBackClick()
        }
    }

    SelectLocationContent(
        location = uiState.location,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun SelectLocationContent(
    location: LatLng?,
    onAction: (SelectLocationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cameraPositionState = rememberCameraPositionState()

    Scaffold(
        modifier = modifier,
        topBar = {
            SelectLocationTopBar(onBackClick = { onAction(SelectLocationAction.OnBackClick) })
        },
        floatingActionButton = {
            LocationSelectionButton(
                onClick = {
                    location?.let {
                        onAction(SelectLocationAction.OnSelectLocationClick(it))
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        NaverMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            cameraPositionState = cameraPositionState,
            uiSettings = remember {
                MapUiSettings(
                    isCompassEnabled = true,
                    isZoomControlEnabled = true,
                    isLocationButtonEnabled = true
                )
            },
            onMapClick = { _, latLng ->
                onAction(SelectLocationAction.OnSelectLocationClick(latLng))
                cameraPositionState.move(
                    update = CameraUpdate
                        .scrollTo(latLng)
                        .animate(CameraAnimation.Fly, CAMERA_ANIMATION_DURATION)
                )
            }
        ) {
            location?.let { location ->
                Marker(
                    state = MarkerState(position = location),
                    width = markerWidth,
                    height = markerHeight,
                    onClick = {
                        onAction(SelectLocationAction.OnSelectLocationClick(null))
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