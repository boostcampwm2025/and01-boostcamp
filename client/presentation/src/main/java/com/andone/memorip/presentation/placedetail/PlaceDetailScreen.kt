package com.andone.memorip.presentation.placedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.andone.memorip.navigation.PlaceDetail
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.placedetail.PlaceDetailScreenConstants.IMAGE_ASPECT_RATIO
import com.andone.memorip.presentation.placedetail.component.ImageDialog
import com.andone.memorip.presentation.placedetail.component.PlaceDetailInfoSection
import com.andone.memorip.presentation.placedetail.component.PlaceDetailTopBar
import com.andone.memorip.presentation.placedetail.model.PlaceDetailAction
import com.andone.memorip.presentation.placedetail.model.PlaceDetailEvent
import com.andone.memorip.presentation.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
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
    viewModel: PlaceDetailViewModel = PlaceDetailViewModel(route) // TODO: hiltViewModel() 적용
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
}

@Composable
private fun PlaceDetailScreen(
    place: PlaceUiModel,
    onAction: (PlaceDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(pageCount = { place.imageUrls.size })

    var imageDialogExpanded by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier,
        topBar = {
            PlaceDetailTopBar(onNavigationIconClick = { onAction(PlaceDetailAction.OnBackClick) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(MemoripPadding.PaddingMedium)
        ) {
            Text(
                text = place.title,
                style = MemoripTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(MemoripSpace.SpaceMedium))
            Column(verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)) {
                Text(
                    text = place.category,
                    style = MemoripTheme.typography.labelLarge
                )
                PlaceDetailInfoSection(
                    infoString = place.locationName,
                    iconRes = R.drawable.ic_location_on
                )
            }

            Spacer(modifier = Modifier.height(MemoripSpace.SpaceMedium))
            PlaceImagesSection(
                pagerState = pagerState,
                imageUrls = place.imageUrls,
                onImageClick = { imageUrl ->
                    selectedImageUrl = imageUrl
                    imageDialogExpanded = true
                }
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

            AsyncImage(
                model = imageUrl,
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
                placeholder = ColorPainter(MemoripTheme.colors.offWhite),
                error = ColorPainter(MemoripTheme.colors.offWhite),
                contentScale = ContentScale.Crop
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