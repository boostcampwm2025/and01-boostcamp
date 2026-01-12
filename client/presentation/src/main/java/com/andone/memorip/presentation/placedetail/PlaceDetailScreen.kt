package com.andone.memorip.presentation.placedetail

import android.R.attr.contentDescription
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.andone.memorip.navigation.PlaceDetail
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.component.TagChipRow
import com.andone.memorip.presentation.placedetail.PlaceDetailScreenConstants.IMAGE_ASPECT_RATIO
import com.andone.memorip.presentation.placedetail.component.ImageDialog
import com.andone.memorip.presentation.placedetail.component.PlaceDetailInfoSection
import com.andone.memorip.presentation.placedetail.component.PlaceDetailTopBar
import com.andone.memorip.presentation.placedetail.model.PlaceDetailAction
import com.andone.memorip.presentation.placedetail.model.PlaceDetailEvent
import com.andone.memorip.presentation.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.theme.Black
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.DummyData.place
import com.andone.memorip.presentation.util.collectWithLifecycle
import kotlinx.collections.immutable.ImmutableList

private object PlaceDetailScreenConstants {
    const val IMAGE_ASPECT_RATIO = 1.5f
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
    var isScrolled by remember{ mutableStateOf(false) }

    var imageDialogExpanded by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            PlaceDetailTopBar(onNavigationIconClick = { onAction(PlaceDetailAction.OnBackClick) })
        }
    ) { innerPadding ->
        if (isScrolled) {
            PlaceDetailContent(
                modifier = Modifier.padding(paddingValues = innerPadding),
                onImageClick = { imageUrl ->
                    selectedImageUrl = imageUrl
                    imageDialogExpanded = true
                }
            )
        } else {
            PlaceSummaryContent(
                imageUrl = place.imageUrls.first(),
                title = place.title,
                fullAddress = place.locationName
            )
        }
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
private fun PlaceImagesSection(
    pagerState: PagerState,
    imageUrls: ImmutableList<String>,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { pageIndex ->
            val imageUrl = imageUrls[pageIndex]

            MemoripImage(
                imageUrl = imageUrl,
                contentDescription = stringResource(R.string.place_detail_image_content_description),
                modifier = Modifier
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = { onImageClick(imageUrl) }
                    )
                    .fillMaxWidth()
                    .clip(MemoripTheme.shapes.roundedMedium)
                    .aspectRatio(IMAGE_ASPECT_RATIO)
                    .background(MemoripTheme.colors.offWhite),
            )
        }

        Text(
            text = stringResource(
                R.string.place_detail_image_count,
                pagerState.currentPage + 1,
                imageUrls.size
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(MemoripPadding.PaddingXSmall)
                .clip(MemoripTheme.shapes.roundedXSmall)
                .background(MemoripTheme.colors.background)
                .padding(
                    horizontal = MemoripPadding.PaddingMedium,
                    vertical = MemoripPadding.PaddingXSmall
                ),
            style = MemoripTheme.typography.bodySmall
        )
    }
}

@Composable
private fun PlaceSummaryContent(
    modifier: Modifier = Modifier,
    imageUrl: String,
    title: String,
    fullAddress: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = imageUrl,
            contentDescription = stringResource(R.string.place_detail_image_content_description),
        )

        Column(
            modifier = Modifier.padding(
                start = MemoripPadding.PaddingXXXLarge,
                bottom = MemoripPadding.PaddingXXXLarge
            ),
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
        ) {
            Text(
                text = title,
                color = MemoripTheme.colors.white,
                style = MemoripTheme.typography.headline2
            )
            Row(horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_location_on),
                    tint = MemoripTheme.colors.green,
                    contentDescription = null
                )
                Text(
                    text = fullAddress,
                    color = MemoripTheme.colors.white,
                    style = MemoripTheme.typography.label1
                )
            }
        }
    }
}

@Composable
private fun PlaceDetailContent(
    modifier: Modifier = Modifier,
    onImageClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(pageCount = { place.imageUrls.size })

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(MemoripPadding.PaddingMedium)
    ) {
        Text(
            text = place.title,
            style = MemoripTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(MemoripSpace.SpaceMedium))
        Column(verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)) {
            TagChipRow(tags = place.tags)
            PlaceDetailInfoSection(
                infoString = place.locationName,
                iconRes = R.drawable.ic_location_on
            )
        }

        Spacer(modifier = Modifier.height(MemoripSpace.SpaceMedium))
        PlaceImagesSection(
            pagerState = pagerState,
            imageUrls = place.imageUrls,
            onImageClick = onImageClick
        )

        Spacer(modifier = Modifier.height(MemoripSpace.SpaceXSmall))
        PlaceDetailInfoSection(
            infoString = place.groupName,
            iconRes = R.drawable.ic_folder,
            modifier = Modifier.padding(start = MemoripPadding.PaddingXSmall)
        )

        Spacer(modifier = Modifier.height(MemoripSpace.SpaceMedium))
        Text(
            text = place.content,
            style = MemoripTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailScreenPreview() {
    MemoripTheme {
        PlaceDetailScreen(
            place = DummyData.place,
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceSummaryContentPreview() {
    MemoripTheme {
        PlaceSummaryContent(
            modifier = Modifier.fillMaxSize(),
            imageUrl = "https://picsum.photos/200/50",
            title = "장소 타이틀",
            fullAddress = "광명, 경기도"
        )
    }
}