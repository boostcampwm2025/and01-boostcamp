package com.andone.memorip.presentation.screen.placedetail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.andone.memorip.navigation.PlaceDetail
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.TagChipRow
import com.andone.memorip.presentation.component.dialog.DeleteDialog
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailScreenConstants.BOTTOM_ALPHA
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailScreenConstants.BOTTOM_RATIO
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailScreenConstants.MIDDLE_ALPHA
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailScreenConstants.MIDDLE_RATIO
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailScreenConstants.MIN_HEIGHT_RATE
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailScreenConstants.TOP_ALPHA
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailScreenConstants.TOP_RATIO
import com.andone.memorip.presentation.screen.placedetail.PlaceDetailScreenDimens.OVERLAY_HEIGHT
import com.andone.memorip.presentation.screen.placedetail.component.ContentCard
import com.andone.memorip.presentation.screen.placedetail.component.ImageDialog
import com.andone.memorip.presentation.screen.placedetail.component.LocationCard
import com.andone.memorip.presentation.screen.placedetail.component.PlaceDetailTopBar
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailAction
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailEvent
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.andone.memorip.presentation.util.openMapOrAskApp
import com.andone.memorip.presentation.util.toDp
import com.andone.memorip.presentation.util.toPx

private object PlaceDetailScreenConstants {
    const val MIN_HEIGHT_RATE = 0.5f
    const val TOP_ALPHA = 0f
    const val MIDDLE_ALPHA = 0.8f
    const val BOTTOM_ALPHA = 1f
    const val TOP_RATIO = 0f
    const val MIDDLE_RATIO = 0.5f
    const val BOTTOM_RATIO = 1f
}

private object PlaceDetailScreenDimens {
    val OVERLAY_HEIGHT = 180.dp
}

@Composable
fun PlaceDetailScreen(
    route: PlaceDetail,
    onNavigateBack: () -> Unit,
    onNavigateGroupList: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceDetailViewModel = hiltViewModel<PlaceDetailViewModel, PlaceDetailViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(route)
        }
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var imageDialogExpanded by rememberSaveable { mutableStateOf(value = false) }
    var selectedImageUrl by rememberSaveable { mutableStateOf(value = "") }
    var showMoreMenu by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            PlaceDetailEvent.NavigateBack -> {
                onNavigateBack()
            }

            PlaceDetailEvent.NavigateToSelectGroup -> {
            }

            }

            PlaceDetailEvent.NavigateToGroupList -> {
                showMoreMenu = false
                showDeleteDialog = true
                onNavigateGroupList()
            }

            PlaceDetailEvent.ShowMoreMenu -> {
                showMoreMenu = true
            }

            PlaceDetailEvent.HideMoreMenu -> {
                showMoreMenu = false
            }

            PlaceDetailEvent.ShowDeleteDialog -> {
                showMoreMenu = false
                showDeleteDialog = true
            }

            PlaceDetailEvent.HideDeleteDialog -> {
                showDeleteDialog = false
            }
        }
    }

    if (uiState.isLoading) {
        LoadingIndicatorScreen()
    } else {
        PlaceDetailContent(
            place = uiState.place,
            showMoreMenu = showMoreMenu,
            onImageClick = {
                imageDialogExpanded = true
                selectedImageUrl = it
            },
            onAction = viewModel::onAction,
            modifier = modifier
                .fillMaxSize()
                .background(color = MemoripTheme.colors.white),
            onNavigateToExternalMap = { locationUiModel ->
                context.openMapOrAskApp(
                    location = locationUiModel,
                )
            },
        )
    }

    if (imageDialogExpanded) {
        ImageDialog(
            imageUrl = selectedImageUrl,
            onDismissRequest = {
                imageDialogExpanded = false
                selectedImageUrl = ""
            },
            modifier = modifier.fillMaxSize()
        )
    }

    if (showDeleteDialog) {
        DeleteDialog(
            title = stringResource(R.string.place_detail_delete_dialog_title),
            content = stringResource(R.string.place_detail_delete_dialog_message),
            onDeleteClick = { viewModel.onAction(PlaceDetailAction.OnDeleteConfirm) },
            onCancelClick = { viewModel.onAction(PlaceDetailAction.OnDeleteDismiss) },
            onDismissRequest = { viewModel.onAction(PlaceDetailAction.OnDeleteDismiss) }
        )
    }
}

@Composable
private fun PlaceDetailContent(
    place: PlaceUiModel,
    showMoreMenu: Boolean,
    onImageClick: (String) -> Unit,
    onAction: (PlaceDetailAction) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToExternalMap: (LocationUiModel) -> Unit = {}
) {
    val density = LocalDensity.current
    val pagerState = rememberPagerState(pageCount = { place.imageUrls.size })
    val scrollState = rememberScrollState()
    val statusBarHeightDp = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val maxHeaderHeight = LocalWindowInfo.current.containerDpSize.height - statusBarHeightDp
    val minHeaderHeight = maxHeaderHeight * MIN_HEIGHT_RATE
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

    Scaffold(
        modifier = modifier,
        topBar = {
            PlaceDetailTopBar(
                isMine = place.isMine,
                isInMyGroup = place.isInMyGroup,
                showMoreMenu = showMoreMenu,
                onNavigationIconClick = { onAction(PlaceDetailAction.OnBackClick) },
                onActionIconClick = { onAction(PlaceDetailAction.OnAddToGroupClick) },
                onMoreClick = { onAction(PlaceDetailAction.OnMoreClick) },
                onMoreMenuDismiss = { onAction(PlaceDetailAction.OnMoreMenuDismiss) },
                onEditClick = { onAction(PlaceDetailAction.OnEditClick) },
                onDeleteClick = { onAction(PlaceDetailAction.OnDeleteClick) }
            )
        },
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(color = MemoripTheme.colors.background)
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXXLarge)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = headerHeightPx.toDp(density = density))
                    .clipToBounds()
                    .clickable { onImageClick(place.imageUrls[pagerState.currentPage]) },
                contentAlignment = Alignment.BottomStart
            ) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = pagerState,
                    key = { idx -> place.imageUrls[idx] }
                ) { idx ->
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = place.imageUrls[idx],
                        contentDescription = stringResource(R.string.place_detail_image_content_description),
                        contentScale = ContentScale.Crop,
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = OVERLAY_HEIGHT)
                        .padding(top = MemoripPadding.PaddingXXXLarge)
                        .background(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    TOP_RATIO to MemoripTheme.colors.background.copy(alpha = TOP_ALPHA),
                                    MIDDLE_RATIO to MemoripTheme.colors.background.copy(alpha = MIDDLE_ALPHA),
                                    BOTTOM_RATIO to MemoripTheme.colors.background.copy(alpha = BOTTOM_ALPHA)
                                )
                            )
                        )
                        .padding(
                            horizontal = MemoripPadding.AppHorizontalPadding,
                            vertical = MemoripPadding.PaddingMedium
                        ),
                    verticalArrangement = Arrangement.spacedBy(
                        alignment = Alignment.Bottom,
                        space = MemoripSpace.SpaceXSmall
                    )
                ) {
                    Text(
                        text = place.title,
                        color = MemoripTheme.colors.onSurface,
                        style = MemoripTheme.typography.headlineBold32
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_location_on),
                            tint = MemoripTheme.colors.primary,
                            contentDescription = null
                        )
                        Text(
                            text = place.locationName,
                            color = MemoripTheme.colors.onSurface,
                            style = MemoripTheme.typography.bodyMedium14
                        )
                    }
                    TagChipRow(tags = place.tags)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MemoripPadding.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)
            ) {
                if (place.content.isNotEmpty()) {
                    ContentCard(content = place.content)
                }
                LocationCard(
                    location = place.locationName,
                    latitude = place.latitude,
                    longitude = place.longitude,
                    onNavigateToExternalMap = {
                        onNavigateToExternalMap(
                            LocationUiModel(
                                id = "",
                                name = place.locationName,
                                category = "",
                                address = place.locationName,
                                roadAddress = "",
                                latitude = place.latitude,
                                longitude = place.longitude
                            )
                        )
                    }
                )
                /** TODO 로그인 기능 구현 시 나의 장소만 그룹 보이도록 수정하기 */
                PlaceDetailInfoSection(
                    infoString = place.groups.joinToString(
                        separator = stringResource(R.string.place_detail_comma_separator)
                    ) { it.groupName },
                    iconRes = R.drawable.ic_folder,
                    onAction = onAction,
                    modifier = Modifier.padding(start = MemoripPadding.PaddingXSmall)
                )
            }
        }
    }
}

@Composable
private fun PlaceDetailInfoSection(
    infoString: String,
    @DrawableRes iconRes: Int,
    onAction: (PlaceDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = { onAction(PlaceDetailAction.GroupClick) },
        modifier = modifier,
        color = MemoripTheme.colors.background
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null
            )
            Text(
                text = infoString,
                style = MemoripTheme.typography.bodyMedium16
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailScreenPreview() {
    MemoripTheme {
        PlaceDetailScreen(
            route = PlaceDetail(""),
            onNavigateBack = {},
            onNavigateSelectGroup = {},
            onNavigateToPlaceEdit = {},
            onNavigateGroupList = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailContentPreview() {
    MemoripTheme {
        PlaceDetailContent(
            place = DummyData.place,
            showMoreMenu = false,
            onImageClick = {},
            onAction = {}
        )
    }
}