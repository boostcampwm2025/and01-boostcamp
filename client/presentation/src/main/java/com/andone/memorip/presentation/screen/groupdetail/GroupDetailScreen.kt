package com.andone.memorip.presentation.screen.groupdetail

import android.graphics.Bitmap
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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.groupdetail.component.GalleryTab
import com.andone.memorip.presentation.screen.groupdetail.component.GroupDetailAppBar
import com.andone.memorip.presentation.screen.groupdetail.component.MapTab
import com.andone.memorip.presentation.screen.groupdetail.component.PlaceImagesBottomSheet
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailAction
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailEvent
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData.places
import com.andone.memorip.presentation.util.collectWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private object MarkerImageConstants {
    const val WIDTH = 200
    const val HEIGHT = 200
    val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
}

@Composable
fun GroupDetailScreen(
    onBackClick: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        places = uiState.places,
        currentPage = uiState.currentTab,
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
    places: ImmutableList<Place>,
    currentPage: Int,
    onAction: (GroupDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val tabs = listOf(
        stringResource(R.string.groupdetail_tab_gallery),
        stringResource(R.string.groupdetail_tab_map)
    )

    val markerImages = remember { mutableStateMapOf<String, Bitmap>() }

    /** 이미지 링크가 들어오면 삭제될 로직 */
    LaunchedEffect(places) {
        places.forEach { place ->
            val imageUrl = place.thumbnailImage.url

            launch(Dispatchers.IO) {
                runCatching {
                    val imageLoader = ImageLoader(context)
                    val request = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .size(MarkerImageConstants.WIDTH, MarkerImageConstants.HEIGHT)
                        .allowHardware(false)
                        .build()
                    imageLoader.execute(request)
                }.mapCatching { result ->
                    (result as? SuccessResult)?.drawable?.toBitmap(
                        width = MarkerImageConstants.WIDTH,
                        height = MarkerImageConstants.HEIGHT,
                        config = MarkerImageConstants.BITMAP_CONFIG
                    ) ?: error("이미지 로드 실패")
                }.onSuccess { bitmap ->
                    withContext(Dispatchers.Main) {
                        markerImages[imageUrl] = bitmap
                    }
                }
            }
        }
    }

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
                modifier = Modifier.fillMaxWidth()
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
                0 -> GalleryTab(
                    places = places,
                    onImageClick = { id -> onAction(GroupDetailAction.OnPlaceClick(id = id)) },
                    modifier = Modifier.fillMaxSize()
                )

                1 -> MapTab(
                    places = places,
                    markerImages = markerImages,
                    onShowBottomSheet = { place -> onAction(GroupDetailAction.OnPictureClick(place = place)) },
                    onDismissBottomSheet = { onAction(GroupDetailAction.OnDismissBottomSheetClick) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(name = "Gallery Tab Selected")
@Composable
private fun GroupDetailScreenContentGalleryPreview() {
    MemoripTheme {
        GroupDetailScreenContent(
            groupName = "Group1",
            places = places.toImmutableList(),
            currentPage = 0,
            onAction = {}
        )
    }
}

@Preview(name = "Map Tab Selected")
@Composable
private fun GroupDetailScreenContentMapPreview() {
    MemoripTheme {
        GroupDetailScreenContent(
            groupName = "Group1",
            places = places.toImmutableList(),
            onAction = { },
            currentPage = 1
        )
    }
}