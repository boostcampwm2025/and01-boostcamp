package com.andone.memorip.presentation.screen.groupdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.andone.memorip.navigation.GroupDetail
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.map.rememberBitmapMarkerLoader
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.groupdetail.component.GroupDetailTopBar
import com.andone.memorip.presentation.screen.groupdetail.component.MapTab
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailAction
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailEvent
import com.andone.memorip.presentation.screen.groupdetail.model.MapBottomSheetStep
import com.andone.memorip.presentation.screen.placelist.PlaceListGrid
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun GroupDetailScreen(
    route: GroupDetail,
    onBackClick: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupDetailViewModel = hiltViewModel<GroupDetailViewModel, GroupDetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(route) }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val placesPagingItems = viewModel.placesPagingFlow.collectAsLazyPagingItems()
    var places by remember { mutableStateOf<List<Place>>(emptyList()) }

    LaunchedEffect(placesPagingItems.itemCount) {
        val newPlaces = mutableListOf<Place>()
        for (i in 0 until placesPagingItems.itemCount) {
            placesPagingItems[i]?.let { place ->
                newPlaces.add(place)
            }
        }
        places = newPlaces
    }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            GroupDetailEvent.NavigateBack -> {
                onBackClick()
            }

            is GroupDetailEvent.NavigateToPlaceDetail -> {
                onImageClick(event.id)
            }
        }
    }

    GroupDetailScreenContent(
        groupName = uiState.groupName,
        currentPage = uiState.currentTab,
        places = places,
        placesPagingItems = placesPagingItems,
        mapSelectedPlace = uiState.mapSelectedPlace,
        mapBottomSheetContent = uiState.mapBottomSheetContent,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
private fun GroupDetailScreenContent(
    groupName: String,
    currentPage: Int,
    places: List<Place>,
    placesPagingItems: LazyPagingItems<Place>,
    mapSelectedPlace: Place?,
    mapBottomSheetContent: MapBottomSheetStep,
    onAction: (GroupDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        R.drawable.ic_image to R.string.groupdetail_tab_gallery,
        R.drawable.ic_map to R.string.groupdetail_tab_map
    )

    var mapLoaded by rememberSaveable { mutableStateOf(false) }
    val markerImages = rememberBitmapMarkerLoader(
        imageUrls = places.map { it.thumbnailImage.url }
    )

    Scaffold(
        topBar = {
            GroupDetailTopBar(
                title = groupName,
                onBackClick = { onAction(GroupDetailAction.OnBackClick) },
                onMenuClick = { onAction(GroupDetailAction.OnMenuClick) },
                onSearchClick = { onAction(GroupDetailAction.OnSearchClick) }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SecondaryTabRow(
                selectedTabIndex = currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MemoripTheme.colors.background,
                contentColor = MemoripTheme.colors.onSurface
            ) {
                tabs.forEachIndexed { index, (iconRes, _) ->
                    Tab(
                        selected = currentPage == index,
                        onClick = { onAction(GroupDetailAction.OnTabClick(currentTab = index)) },
                        icon = {
                            Icon(
                                painter = painterResource(iconRes),
                                contentDescription = stringResource(tabs[index].second),
                                tint = if (currentPage == index) {
                                    MemoripTheme.colors.primary
                                } else {
                                    MemoripTheme.colors.gray
                                }
                            )
                        }
                    )
                }
            }

            when (currentPage) {
                0 -> PlaceListGrid(
                    placePagingItems = placesPagingItems,
                    onPlaceClick = { id -> onAction(GroupDetailAction.OnPlaceClick(id = id)) },
                    onRefresh = { /* GroupDetail에서는 refresh 불필요 */ },
                    modifier = Modifier.fillMaxSize()
                        .padding(vertical = MemoripPadding.AppHorizontalPadding)
                )

                1 -> MapTab(
                    places = places,
                    markerImages = markerImages,
                    mapBottomSheetContent = mapBottomSheetContent,
                    mapLoaded = mapLoaded,
                    onMapLoaded = { mapLoaded = true },
                    onAction = onAction,
                    modifier = Modifier.fillMaxSize(),
                    mapSelectedPlace = mapSelectedPlace
                )
            }
        }
    }
}