package com.andone.memorip.presentation.screen.groupdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.andone.memorip.presentation.screen.groupdetail.component.GroupDetailAppBar
import com.andone.memorip.presentation.screen.groupdetail.component.MapTab
import com.andone.memorip.presentation.screen.groupdetail.component.PlaceImagesBottomSheet
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailAction
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailEvent
import com.andone.memorip.presentation.screen.placelist.PlaceListGrid
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun GroupDetailScreen(
    route: GroupDetail,
    onBackClick: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupDetailViewModel = hiltViewModel<GroupDetailViewModel, GroupDetailViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(route)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val placesPagingItems = viewModel.placesPagingFlow.collectAsLazyPagingItems()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            GroupDetailEvent.NavigateBack -> {
                onBackClick()
            }
            is GroupDetailEvent.NavigatePlaceDetail -> {
                onImageClick(event.id)
            }
        }
    }

    GroupDetailScreenContent(
        groupName = uiState.groupName,
        currentPage = uiState.currentTab,
        placesPagingItems = placesPagingItems,
        onAction = viewModel::onAction,
        modifier = modifier
    )

    if (uiState.selectedPlace != null) {
        PlaceImagesBottomSheet(
            placeName = uiState.selectedPlace!!.name,
            images = uiState.selectedPlace!!.images,
            onDismiss = { viewModel.onAction(GroupDetailAction.OnDismissBottomSheetClick) }
        )
    }
}

@Composable
fun GroupDetailScreenContent(
    groupName: String,
    currentPage: Int,
    placesPagingItems: LazyPagingItems<Place>,
    onAction: (GroupDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        stringResource(R.string.groupdetail_tab_gallery),
        stringResource(R.string.groupdetail_tab_map)
    )

    var places by remember { mutableStateOf<List<Place>>(emptyList()) }
    var mapLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(placesPagingItems.itemCount) {
        val newPlaces = mutableListOf<Place>()
        for (i in 0 until placesPagingItems.itemCount) {
            placesPagingItems[i]?.let { place ->
                newPlaces.add(place)
            }
        }
        places = newPlaces
    }

    val markerImages = rememberBitmapMarkerLoader(
        imageUrls = places.map { it.thumbnailImage.url }
    )

    Scaffold(
        topBar = {
            GroupDetailAppBar(
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
            PrimaryTabRow(
                selectedTabIndex = currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = MemoripTheme.colors.background,
                contentColor = MemoripTheme.colors.onSurface
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = currentPage == index,
                        onClick = { onAction(GroupDetailAction.OnTabClick(currentTab = index)) },
                        text = { Text(text = title) }
                    )
                }
            }

            when (currentPage) {
                 0 -> {
                    PlaceListGrid(
                        placePagingItems = placesPagingItems,
                        onPlaceClick = { id -> onAction(GroupDetailAction.OnPlaceClick(id = id)) },
                        onRefresh = { /* GroupDetail에서는 refresh 불필요 */ },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                1 -> MapTab(
                    places = places,
                    markerImages = markerImages,
                    onPlaceClick = { id -> onAction(GroupDetailAction.OnPlaceClick(id = id)) },
                    mapLoaded = mapLoaded,
                    onMapLoaded = { mapLoaded = true },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}