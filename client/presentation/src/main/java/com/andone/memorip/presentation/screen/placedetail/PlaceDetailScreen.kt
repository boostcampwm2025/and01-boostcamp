package com.andone.memorip.presentation.placedetail

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andone.memorip.navigation.PlaceDetail
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.placedetail.component.ContentCard
import com.andone.memorip.presentation.placedetail.component.ContrastAwareText
import com.andone.memorip.presentation.screen.placedetail.component.LocationCard
import com.andone.memorip.presentation.placedetail.component.TagCard
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailViewModel
import com.andone.memorip.presentation.screen.placedetail.component.ImageDialog
import com.andone.memorip.presentation.screen.placedetail.component.PlaceDetailInfoSection
import com.andone.memorip.presentation.screen.placedetail.component.PlaceDetailTopBar
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailAction
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailEvent
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

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
            innerPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .background(color = MemoripTheme.colors.white),
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
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val maxHeaderHeight =
        LocalWindowInfo.current.containerDpSize.height - innerPadding.calculateTopPadding() - innerPadding.calculateBottomPadding()
    val minHeaderHeight = maxHeaderHeight * 0.3f
    val maxHeaderPx = with(density) { maxHeaderHeight.toPx() }
    val minHeaderPx = with(density) { minHeaderHeight.toPx() }
    val headerHeightPx = remember(scrollState.value) {
        (maxHeaderPx - scrollState.value).coerceIn(minHeaderPx, maxHeaderPx)
    }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val pagerState = rememberPagerState(pageCount = { place.imageUrls.size })

    Column(
        modifier = modifier
            .background(color = MemoripTheme.colors.background)
            .verticalScroll(state = scrollState)
            .padding(paddingValues = innerPadding)
            .padding(bottom = MemoripPadding.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXXLarge)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = with(receiver = density) { headerHeightPx.toDp() })
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
                    model = ImageRequest.Builder(context)
                        .data(data = place.imageUrls[idx])
                        .allowHardware(enable = false)
                        .build(),
                    contentDescription = stringResource(R.string.place_detail_image_content_description),
                    contentScale = ContentScale.Crop,
                    onSuccess = { result ->
                        bitmap = (result.result.drawable as BitmapDrawable).bitmap
                    }
                )
            }

            Column(
                modifier = Modifier.padding(
                    start = MemoripPadding.PaddingXXXLarge,
                    bottom = MemoripPadding.PaddingXXXLarge
                ),
                verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
            ) {
                ContrastAwareText(
                    image = bitmap,
                    text = place.title,
                    style = MemoripTheme.typography.headline2
                )
                Row(horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_location_on),
                        tint = MemoripTheme.colors.green,
                        contentDescription = null
                    )
                    ContrastAwareText(
                        image = bitmap,
                        text = place.locationName,
                        style = MemoripTheme.typography.label1
                    )
                }
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
            TagCard(tags = place.tags)
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
            innerPadding = PaddingValues(0.dp),
            onImageClick = {},
        )
    }
}