package com.andone.memorip.presentation.screen.placecreate

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.component.MemoripInputBox
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.screen.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.screen.placecreate.PlaceCreateScreenConstant.CONTENT_MAX_LENGTH
import com.andone.memorip.presentation.screen.placecreate.PlaceCreateScreenConstant.IMAGE_RATIO
import com.andone.memorip.presentation.screen.placecreate.PlaceCreateScreenConstant.TITLE_MAX_LENGTH
import com.andone.memorip.presentation.screen.placecreate.component.ImageCountCard
import com.andone.memorip.presentation.screen.placecreate.component.PlaceCreateBottomBar
import com.andone.memorip.presentation.screen.placecreate.component.SelectRow
import com.andone.memorip.presentation.screen.placecreate.component.SelectedImageItem
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateEvent
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateUiState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.naver.maps.map.compose.ExperimentalNaverMapApi

private object PlaceCreateScreenConstant {
    const val TITLE_MAX_LENGTH = 30
    const val CONTENT_MAX_LENGTH = 300
    const val IMAGE_RATIO = 1.5f
}

@Composable
fun PlaceCreateScreen(
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onGroupClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            PlaceCreateEvent.NavigateToHome -> onNavigateToHome()
            PlaceCreateEvent.NavigateToCategory -> onCategoryClick()
            PlaceCreateEvent.NavigateToLocation -> onLocationClick()
            PlaceCreateEvent.NavigateToGroup -> onGroupClick()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        PlaceCreateScreenContent(
            uiState = uiState,
            onAction = viewModel::onAction
        )

        if (uiState.isLoading) {
            LoadingIndicatorScreen(
                modifier = Modifier.clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {}
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceCreateScreenContent(
    uiState: PlaceCreateUiState,
    onAction: (PlaceCreateAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState(uiState.scrollPosition)

    val placeCreateEnable = uiState.images.isNotEmpty() &&
            uiState.location != null &&
            uiState.title.isNotBlank() &&
            uiState.group != null

    LaunchedEffect(Unit) {
        onAction(PlaceCreateAction.OnImageSelect(uiState.images.first()))
    }

    LaunchedEffect(uiState.images) {
        if (uiState.images.isEmpty()) {
            onAction(PlaceCreateAction.OnLastImageRemove)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onAction(PlaceCreateAction.OnScrollPositionChange(scrollState.value))
        }
    }

    Scaffold(
        bottomBar = {
            PlaceCreateBottomBar(
                value = stringResource(R.string.place_create_button_text),
                onClick = { onAction(PlaceCreateAction.OnPlaceCreate(context)) },
                enabled = placeCreateEnable
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = MemoripPadding.AppHorizontalPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXLarge)
        ) {
            ImageRowSection(
                images = uiState.images,
                selectedImage = uiState.selectedImage,
                onImageSelect = { uri -> onAction(PlaceCreateAction.OnImageSelect(uri)) },
                onRemoveImage = { uri -> onAction(PlaceCreateAction.OnImagesRemove(uri)) }
            )

            ContentSection(
                title = uiState.title,
                content = uiState.content,
                onTitleChange = { onAction(PlaceCreateAction.OnTitleChange(it)) },
                onContentChange = { onAction(PlaceCreateAction.OnContentChange(it)) },
            )

            SelectSection(
                category = uiState.category,
                location = uiState.location,
                group = uiState.group,
                onCategoryClick = { onAction(PlaceCreateAction.OnCategoryClick) },
                onLocationClick = { onAction(PlaceCreateAction.OnLocationClick) },
                onGroupClick = { onAction(PlaceCreateAction.OnGroupClick) },
            )

            PublicCheckSection(
                isPublic = uiState.isPublic,
                onCheckedChange = { onAction(PlaceCreateAction.OnPublicChange) }
            )
        }
    }
}

@Composable
private fun ImageRowSection(
    images: List<Uri>,
    selectedImage: Uri?,
    onImageSelect: (Uri) -> Unit,
    onRemoveImage: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(IMAGE_RATIO),
            horizontalArrangement = Arrangement.Center
        ) {
            Box {
                MemoripImage(
                    imageUrl = selectedImage.toString(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                ImageCountCard(
                    currentImageIndex = images.indexOf(selectedImage),
                    totalImageCount = images.size,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }

        Row(
            modifier = Modifier.horizontalScroll(state = rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            images.forEach { uri ->
                SelectedImageItem(
                    imageUri = uri,
                    isSelected = selectedImage == uri,
                    onClick = { onImageSelect(uri) },
                    onRemoveClick = { onRemoveImage(uri) }
                )
            }
        }
    }
}

@Composable
private fun ContentSection(
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        MemoripInputBox(
            value = title,
            valueMaxLength = TITLE_MAX_LENGTH,
            placeholder = stringResource(R.string.place_create_title_input),
            onValueChange = onTitleChange,
            singleLine = true,
            showValueLength = false,
            height = OutlinedTextFieldDefaults.MinHeight
        )

        HorizontalDivider(color = MemoripTheme.colors.gray1)

        MemoripInputBox(
            value = content,
            valueMaxLength = CONTENT_MAX_LENGTH,
            placeholder = stringResource(R.string.place_create_content_input),
            onValueChange = onContentChange,
        )
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
private fun SelectSection(
    category: List<TagUiModel>,
    location: LocationUiModel?,
    group: GroupUiModel?,
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onGroupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locationValue = location?.name
        ?.ifBlank { null } ?: stringResource(R.string.place_create_location_placeholder)
    val groupValue = group?.name
        ?: stringResource(R.string.place_create_group_placeholder)
    val categoryValue = category
        .joinToString(stringResource(R.string.place_create_space)) { it.name }
        .ifEmpty { stringResource(R.string.place_create_tag_placeholder) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXLarge)
    ) {
        SelectRow(
            label = stringResource(R.string.place_create_location),
            value = locationValue,
            leadingIcon = painterResource(R.drawable.ic_location_on),
            onClick = onLocationClick,
            location = location
        )

        SelectRow(
            label = stringResource(R.string.place_create_group),
            value = groupValue,
            leadingIcon = painterResource(R.drawable.ic_folder),
            onClick = onGroupClick,
            trailingIcon = painterResource(R.drawable.ic_chevron_forward)
        )

        SelectRow(
            label = stringResource(R.string.place_create_tag),
            value = categoryValue,
            leadingIcon = painterResource(R.drawable.ic_tag),
            onClick = onCategoryClick,
            trailingIcon = painterResource(R.drawable.ic_chevron_forward)
        )
    }
}

@Composable
private fun PublicCheckSection(
    isPublic: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_globe),
                contentDescription = null,
                tint = MemoripTheme.colors.gray1
            )
            Text(
                text = stringResource(R.string.place_create_open_to_everyone),
                color = MemoripTheme.colors.gray1,
                style = MemoripTheme.typography.bodyMedium14
            )
        }

        Switch(
            checked = isPublic,
            onCheckedChange = { onCheckedChange() },
            colors = SwitchDefaults.colors(
                checkedTrackColor = MemoripTheme.colors.primary,
                checkedBorderColor = MemoripTheme.colors.white,
                uncheckedThumbColor = MemoripTheme.colors.white,
                uncheckedTrackColor = MemoripTheme.colors.gray1,
                uncheckedBorderColor = MemoripTheme.colors.white
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceCreateScreenContentsPreview() {
    MemoripTheme {
        Column {
            PlaceCreateScreenContent(
                uiState = PlaceCreateUiState(
                    images = DummyData.groups.first().images.take(3).map { it.toUri() }
                ),
                onAction = {}
            )
        }
    }
}