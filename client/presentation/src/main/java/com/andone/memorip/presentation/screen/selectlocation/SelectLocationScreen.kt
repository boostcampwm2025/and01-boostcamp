package com.andone.memorip.presentation.screen.selectlocation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.EmptyText
import com.andone.memorip.presentation.component.MemoripPagingList
import com.andone.memorip.presentation.component.MemoripSearchBarInputField
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.screen.selectlocation.SelectLocationScreenConstants.CAMERA_ANIMATION_DURATION
import com.andone.memorip.presentation.screen.selectlocation.SelectLocationScreenConstants.CAMERA_POSITION_ZOOM
import com.andone.memorip.presentation.screen.selectlocation.SelectLocationScreenDimens.markerHeight
import com.andone.memorip.presentation.screen.selectlocation.SelectLocationScreenDimens.markerWidth
import com.andone.memorip.presentation.screen.selectlocation.component.LocationItem
import com.andone.memorip.presentation.screen.selectlocation.component.LocationSelectionButton
import com.andone.memorip.presentation.screen.selectlocation.model.SelectLocationAction
import com.andone.memorip.presentation.screen.selectlocation.model.SelectLocationEvent
import com.andone.memorip.presentation.screen.selectlocation.model.SelectLocationUiState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
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
    const val CAMERA_POSITION_ZOOM = 16.0
}

@Composable
fun SelectLocationScreen(
    onLocationSelect: (LocationUiModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SelectLocationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val locations = viewModel.locationsPagingFlow.collectAsLazyPagingItems()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            is SelectLocationEvent.SelectLocation -> onLocationSelect(event.location)
        }
    }

    SelectLocationContent(
        uiState = uiState,
        locations = locations,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@OptIn(ExperimentalNaverMapApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SelectLocationContent(
    uiState: SelectLocationUiState,
    locations: LazyPagingItems<LocationUiModel>,
    onAction: (SelectLocationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val cameraPositionState = uiState.location?.let {
        rememberCameraPositionState {
            position = CameraPosition(LatLng(it.latitude, it.longitude), CAMERA_POSITION_ZOOM)
        }
    } ?: run {
        rememberCameraPositionState()
    }

    var searchBarExpanded by rememberSaveable { mutableStateOf(false) }

    fun cameraPositionMove(latLng: LatLng) {
        cameraPositionState.move(
            CameraUpdate
                .scrollTo(latLng)
                .animate(CameraAnimation.Fly, CAMERA_ANIMATION_DURATION)
        )
    }

    SearchBar(
        inputField = {
            MemoripSearchBarInputField(
                query = uiState.query,
                expanded = searchBarExpanded,
                onQueryChange = { onAction(SelectLocationAction.OnQueryChange(it)) },
                onExpandedChange = { searchBarExpanded = it },
                modifier = Modifier.fillMaxWidth()
            )
        },
        expanded = searchBarExpanded,
        onExpandedChange = { searchBarExpanded = it },
        modifier = modifier,
        colors = SearchBarDefaults.colors(containerColor = MemoripTheme.colors.white),
        windowInsets = WindowInsets()
    ) {
        MemoripPagingList(
            pagingItems = locations,
            itemKey = { it.id },
            modifier = Modifier.fillMaxSize(),
            initialContent = {
                EmptyText(
                    text = stringResource(R.string.search_bar_placeholder),
                    modifier = Modifier.fillMaxSize()
                )
            },
            emptyContent = {
                EmptyText(
                    text = stringResource(R.string.search_bar_empty_result),
                    modifier = Modifier.fillMaxSize()
                )
            },
            itemContent = { location ->
                LocationItem(
                    location = location,
                    onClick = {
                        onAction(SelectLocationAction.OnLocationClick(location))
                        cameraPositionMove(LatLng(location.latitude, location.longitude))
                        searchBarExpanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                onAction(SelectLocationAction.OnMapClick(latLng))
                cameraPositionMove(latLng)
            }
        ) {
            uiState.location?.let { location ->
                Marker(
                    state = MarkerState(
                        position = LatLng(location.latitude, location.longitude)
                    ),
                    width = markerWidth,
                    height = markerHeight,
                    onClick = {
                        onAction(SelectLocationAction.OnLocationClick(null))
                        true
                    }
                )
            }
        }

        LocationSelectionButton(
            onClick = {
                uiState.location?.let { onAction(SelectLocationAction.OnLocationSelect(it)) }
            },
            enabled = uiState.location != null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = MemoripPadding.PaddingMedium)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SelectLocationScreenPreview() {
    MemoripTheme {
        SelectLocationScreen(onLocationSelect = {})
    }
}