package com.andone.memorip.presentation.screen.tripdetail.component

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.map.InteractiveMultiMarkerMapView
import com.andone.memorip.presentation.component.map.MapClusterManager
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.tripdetail.model.MapBottomSheetStep
import com.andone.memorip.presentation.screen.tripdetail.model.PlaceViewMode
import com.andone.memorip.presentation.screen.tripdetail.model.TripDetailAction
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripShadow
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.util.DummyData
import com.naver.maps.map.compose.rememberCameraPositionState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.paging.compose.LazyPagingItems

private object TripMapContentDimen {
    val SHEET_PEEK_HEIGHT = 200.dp
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Composable
fun TripMapContent(
    places: List<Place>,
    placesPagingItems: LazyPagingItems<Place>,
    clusteredItems: List<MapClusterManager.ClusterItem>,
    markerImages: Map<String, Bitmap>,
    cameraPositionState: CameraPositionState,
    scaffoldState: BottomSheetScaffoldState,
    isBottomSheetExpanded: Boolean,
    mapLoaded: Boolean,
    mapBottomSheetContent: MapBottomSheetStep,
    mapSelectedPlace: Place?,
    tripName: String?,
    startDate: String?,
    endDate: String?,
    viewMode: PlaceViewMode,
    hasMorePages: Boolean,
    onAction: (TripDetailAction) -> Unit,
    onMarkerClick: (Place) -> Unit,
    onClusterClick: (MapClusterManager.ClusterItem) -> Unit,
    onMapLoaded: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        if (!mapLoaded) {
            LoadingIndicatorScreen(modifier = Modifier.fillMaxSize())
        }

        BottomSheetScaffold(
            sheetContent = {
                MapBottomSheetContent(
                    bottomSheetContent = mapBottomSheetContent,
                    places = places,
                    placesPagingItems = placesPagingItems,
                    selectedPlace = mapSelectedPlace,
                    viewMode = viewMode,
                    hasMorePages = hasMorePages,
                    onAction = onAction,
                    isExpanded = isBottomSheetExpanded
                )
            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = TripMapContentDimen.SHEET_PEEK_HEIGHT,
            sheetShape = if (isBottomSheetExpanded) RectangleShape else BottomSheetDefaults.ExpandedShape,
            sheetContainerColor = MemoripTheme.colors.background,
            sheetDragHandle = null,
            sheetSwipeEnabled = mapBottomSheetContent != MapBottomSheetStep.PlaceDetail,
            modifier = Modifier.fillMaxSize()
        ) {
            InteractiveMultiMarkerMapView(
                initialBounds = places.map { LatLng(it.latitude, it.longitude) },
                onMapLoaded = onMapLoaded,
                contentPadding = PaddingValues(bottom = TripMapContentDimen.SHEET_PEEK_HEIGHT),
                cameraPositionState = cameraPositionState,
                modifier = Modifier.fillMaxSize()
            ) {
                if (mapLoaded) {
                    PlaceImageMarkers(
                        clusteredItems = clusteredItems,
                        markerImages = markerImages,
                        selectedPlace = mapSelectedPlace,
                        onMarkerClick = onMarkerClick,
                        onClusterClick = onClusterClick
                    )
                }
            }
        }

        if (startDate != null && endDate != null && tripName != null) {
            TripScheduleCard(
                tripName = tripName,
                startDate = startDate,
                endDate = endDate,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = MemoripPadding.PaddingMedium,
                        start = MemoripPadding.PaddingMedium,
                        end = MemoripPadding.PaddingMedium
                    )
            )
        }
    }
}

@Composable
private fun MapBottomSheetContent(
    bottomSheetContent: MapBottomSheetStep,
    places: List<Place>,
    placesPagingItems: LazyPagingItems<Place>,
    selectedPlace: Place?,
    viewMode: PlaceViewMode,
    onAction: (TripDetailAction) -> Unit,
    modifier: Modifier = Modifier,
    hasMorePages: Boolean = false,
    isExpanded: Boolean = false
) {
    val placeListState = rememberLazyListState()

    Surface(
        tonalElevation = MemoripShadow.Large,
        shadowElevation = MemoripShadow.Large,
        color = MemoripTheme.colors.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = if (isExpanded) 0.dp else MemoripPadding.PaddingSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isExpanded) {
                Box(
                    modifier = Modifier
                        .width(MemoripPadding.PaddingXXXLarge)
                        .height(MemoripSpace.SpaceXXSmall)
                        .clip(RoundedCornerShape(MemoripLineWidth.Small))
                        .background(MemoripTheme.colors.gray1)
                )
                Spacer(modifier = Modifier.height(MemoripPadding.PaddingXSmall))
            }
            Box(modifier = Modifier.weight(1f)) {
                when (bottomSheetContent) {
                    MapBottomSheetStep.PlaceList -> {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            BottomSheetPlaceListHeader(
                                placeCount = places.size,
                                viewMode = viewMode,
                                hasMorePages = hasMorePages,
                                onViewModeToggle = { onAction(TripDetailAction.OnViewModeToggle) }
                            )

                            when (viewMode) {
                                PlaceViewMode.LIST -> {
                                    BottomSheetPlaceListContent(
                                        placesPagingItems = placesPagingItems,
                                        listState = placeListState,
                                        onAction = onAction
                                    )
                                }

                                PlaceViewMode.GRID -> {
                                    BottomSheetPlaceGridContent(
                                        placesPagingItems = placesPagingItems,
                                        onAction = onAction
                                    )
                                }
                            }
                        }
                    }

                    MapBottomSheetStep.PlaceDetail -> {
                        selectedPlace?.let { place ->
                            BottomSheetPlaceDetailContent(
                                place = place,
                                onCloseClick = { onAction(TripDetailAction.OnPlaceDetailBottomSheetClose) },
                                onDetailClick = {
                                    onAction(
                                        TripDetailAction.OnNavigateToPlaceDetail(
                                            place.placeId
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalNaverMapApi::class)
@Preview(showBackground = true)
@Composable
private fun TripMapContentPreview() {
    MemoripTheme {
        val clusteredItems = DummyData.places.map { place ->
            MapClusterManager.ClusterItem(
                position = LatLng(place.latitude, place.longitude),
                places = listOf(
                    MapClusterManager.PlaceClusterData(
                        id = place.placeId,
                        position = LatLng(place.latitude, place.longitude),
                        imageUrl = place.thumbnailImage.url,
                        placeData = place
                    )
                ),
                isCluster = false
            )
        }

        TripMapContent(
            places = DummyData.places,
            placesPagingItems = DummyData.getPlacePagingItems(),
            clusteredItems = clusteredItems,
            markerImages = emptyMap(),
            cameraPositionState = rememberCameraPositionState(),
            scaffoldState = rememberBottomSheetScaffoldState(),
            isBottomSheetExpanded = false,
            mapLoaded = true,
            mapBottomSheetContent = MapBottomSheetStep.PlaceList,
            mapSelectedPlace = null,
            tripName = "서울 여행",
            startDate = "2026-02-05",
            endDate = "2026-02-07",
            viewMode = PlaceViewMode.LIST,
            hasMorePages = false,
            onAction = {},
            onMarkerClick = {},
            onClusterClick = {},
            onMapLoaded = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
