package com.andone.memorip.presentation.placedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.andone.memorip.navigation.PlaceDetail
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.TagChipRow
import com.andone.memorip.presentation.placedetail.Constants.minHeightRate
import com.andone.memorip.presentation.placedetail.component.ContentCard
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailViewModel
import com.andone.memorip.presentation.screen.placedetail.component.ImageDialog
import com.andone.memorip.presentation.screen.placedetail.component.LocationCard
import com.andone.memorip.presentation.screen.placedetail.component.PlaceDetailInfoSection
import com.andone.memorip.presentation.screen.placedetail.component.PlaceDetailTopBar
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailAction
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailEvent
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.theme.MemoripAlpha
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.andone.memorip.presentation.util.toDp
import com.andone.memorip.presentation.util.toPx

private object Constants {
    const val minHeightRate = 0.5f
}

@Composable
fun PlaceDetailScreen(
    route: PlaceDetail,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceDetailViewModel = hiltViewModel<PlaceDetailViewModel, PlaceDetailViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(route)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            PlaceDetailEvent.NavigateBack -> onNavigateBack()
        }
    }

    PlaceDetailScreen(
        place = uiState.place,
        onAction = viewModel::onAction,
        modifier = modifier
    )

    if (uiState.isLoading) {
        LoadingIndicatorScreen()
    }
}

@Composable
private fun PlaceDetailScreen(
    place: PlaceUiModel,
    onAction: (PlaceDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var imageDialogExpanded by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            PlaceDetailTopBar(
                onNavigationIconClick = { onAction(PlaceDetailAction.OnBackClick) },
                onActionIconClick = { /** TODO 정보 가져오기 */ }
            )
        },
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        PlaceDetailContent(
            place = place,
            onImageClick = {
                imageDialogExpanded = true
                selectedImageUrl = it
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = MemoripTheme.colors.white)
                .padding(bottom = innerPadding.calculateBottomPadding()),
        )
    }

    if (imageDialogExpanded) {
        ImageDialog(
            imageUrl = selectedImageUrl,
            onDismissRequest = {
                imageDialogExpanded = false
                selectedImageUrl = ""
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun PlaceDetailContent(
    place: PlaceUiModel,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    val statusBarHeightDp = WindowInsets.statusBars
        .getTop(density = density).toFloat()
        .toDp(density = density)
    val maxHeaderHeight = LocalWindowInfo.current.containerDpSize.height - statusBarHeightDp
    val minHeaderHeight = maxHeaderHeight * minHeightRate
    val maxHeaderPx = maxHeaderHeight.toPx(density = density)
    val minHeaderPx = minHeaderHeight.toPx(density = density)
    val collapseRangePx = maxHeaderPx - minHeaderPx
    val headerHeightPx by remember {
        derivedStateOf {
            val collapseOffset = scrollState.value.toFloat()
                .coerceIn(0f, collapseRangePx)
            maxHeaderPx - collapseOffset
        }
    }

    val pagerState = rememberPagerState(pageCount = { place.imageUrls.size })

    Column(
        modifier = modifier
            .background(color = MemoripTheme.colors.background)
            .verticalScroll(state = scrollState)
            .padding(bottom = MemoripPadding.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXXLarge)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = headerHeightPx.toDp(density = density))
                .clipToBounds()
                .clickable { place.imageUrls.firstOrNull()?.let { image -> onImageClick(image) } },
            contentAlignment = Alignment.BottomStart
        ) {
            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                key = { idx -> place.imageUrls[idx] }
            ) { idx ->
                AsyncImage(
                    model = place.imageUrls[idx],
                    contentDescription = stringResource(R.string.place_detail_image_content_description),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .padding(
                        start = MemoripPadding.PaddingXXXLarge,
                        bottom = MemoripPadding.PaddingXXXLarge
                    )
                    .background(
                        color = MemoripTheme.colors.surface.copy(alpha = MemoripAlpha.IMAGE_OVERLAY),
                        shape = MemoripTheme.shapes.roundedSmall
                    )
                    .padding(all = MemoripPadding.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
            ) {
                Text(
                    text = place.title,
                    color = MemoripTheme.colors.black,
                    style = MemoripTheme.typography.headline2
                )
                Row(horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_location_on),
                        tint = MemoripTheme.colors.green,
                        contentDescription = null
                    )
                    Text(
                        text = place.locationName,
                        color = MemoripTheme.colors.black,
                        style = MemoripTheme.typography.label1
                    )
                }
                TagChipRow(tags = place.tags)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MemoripPadding.PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceMedium)
        ) {
            ContentCard(content = place.content)
            LocationCard(
                location = place.locationName,
                latitude = place.latitude,
                longitude = place.longitude
            )
            PlaceDetailInfoSection(
                infoString = place.groupName,
                iconRes = R.drawable.ic_folder,
                modifier = Modifier.padding(start = MemoripPadding.PaddingXSmall)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailScreenPrev() {
    MemoripTheme {
        PlaceDetailScreen(
            place = DummyData.place,
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailContentPrev() {
    MemoripTheme {
        PlaceDetailContent(
            place = PlaceUiModel(),
            onImageClick = {},
        )
    }
}