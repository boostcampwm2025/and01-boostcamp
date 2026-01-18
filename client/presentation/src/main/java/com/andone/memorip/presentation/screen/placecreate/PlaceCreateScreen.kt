package com.andone.memorip.presentation.screen.placecreate

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.component.MemoripInputBox
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.screen.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.screen.placecreate.PictureSetting.MAX_PICTURE_COUNT
import com.andone.memorip.presentation.screen.placecreate.component.ImageCountButton
import com.andone.memorip.presentation.screen.placecreate.component.LocationMapPreview
import com.andone.memorip.presentation.screen.placecreate.component.SelectRow
import com.andone.memorip.presentation.screen.placecreate.component.SelectedImageItem
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateEvent
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateUiState
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.naver.maps.map.compose.ExperimentalNaverMapApi

private object PictureSetting {
    const val MAX_PICTURE_COUNT = 10
}

@Composable
fun PlaceCreateScreen(
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onGroupClick: () -> Unit,
    onImageCreate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            PlaceCreateEvent.NavigateToHome -> onImageCreate()
            PlaceCreateEvent.NavigateToCategory -> onCategoryClick()
            PlaceCreateEvent.NavigateToLocation -> onLocationClick()
            PlaceCreateEvent.NavigateToGroup -> onGroupClick()
        }
    }

    PlaceCreateScreenContents(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceCreateScreenContents(
    uiState: PlaceCreateUiState,
    onAction: (PlaceCreateAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(MemoripPadding.PaddingXSmall),
            verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall)
        ) {
            ImageRowSection(
                selectedImages = uiState.images,
                maxCount = MAX_PICTURE_COUNT,
                onRemoveImage = { uri -> onAction(PlaceCreateAction.OnImagesRemove(uri)) }
            )

            ContentSection(
                title = uiState.title,
                content = uiState.content,
                isPublic = uiState.isPublic,
                onTitleChange = { onAction(PlaceCreateAction.OnTitleChange(it)) },
                onContentChange = { onAction(PlaceCreateAction.OnContentChange(it)) },
                onCheckedChange = { onAction(PlaceCreateAction.OnPublicChange) }
            )

            SelectSection(
                category = uiState.category,
                location = uiState.location,
                group = uiState.group,
                onCategoryClick = { onAction(PlaceCreateAction.OnCategoryClick) },
                onLocationClick = { onAction(PlaceCreateAction.OnLocationClick) },
                onGroupClick = { onAction(PlaceCreateAction.OnGroupClick) },
                modifier = Modifier.padding(bottom = MemoripPadding.PaddingMedium)
            )
        }

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

@Composable
private fun ImageRowSection(
    selectedImages: List<Uri>,
    maxCount: Int,
    onRemoveImage: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(state = rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        selectedImages.forEach { uri ->
            SelectedImageItem(
                imageUri = uri,
                onRemoveClick = { onRemoveImage(uri) }
            )
        }

        ImageCountButton(
            current = selectedImages.size,
            max = maxCount,
            modifier = Modifier.padding(vertical = MemoripPadding.PaddingXSmall)
        )
    }
}

@Composable
private fun ContentSection(
    title: String,
    content: String,
    isPublic: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onCheckedChange: () -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.place_create_content_title),
            style = MemoripTheme.typography.title1
        )
        MemoripInputBox(
            label = stringResource(R.string.place_create_content_title),
            value = title,
            placeholder = stringResource(R.string.place_create_title_input),
            onValueChange = onTitleChange,
            onClear = { onTitleChange("") },
            height = MemoripHeight.TextBoxDefault
        )

        Text(
            text = stringResource(R.string.place_create_content),
            style = MemoripTheme.typography.title1
        )
        MemoripInputBox(
            label = stringResource(R.string.place_create_content),
            value = content,
            placeholder = stringResource(R.string.place_create_content_input),
            onValueChange = onContentChange,
            onClear = { onContentChange("") }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.place_create_open_to_everyone),
                style = MemoripTheme.typography.hint1
            )
            Checkbox(
                checked = isPublic,
                onCheckedChange = { onCheckedChange() }
            )
        }
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
    val categoryValue =
        category.joinToString(stringResource(R.string.place_create_join_to_string_comma)) { it.name }
    val locationValue =
        location?.name?.ifEmpty { stringResource(R.string.place_create_location_placeholder) }
    val groupValue = group?.name

    Column(modifier = modifier) {
        SelectRow(
            label = stringResource(R.string.place_create_location),
            value = locationValue,
            leadingIcon = painterResource(R.drawable.ic_location_on),
            onClick = onLocationClick
        )
        LocationMapPreview(
            location = location,
            onLocationClick = onLocationClick,
            modifier = Modifier.weight(1f)
        )
        SelectRow(
            label = stringResource(R.string.place_create_group),
            value = groupValue,
            leadingIcon = painterResource(R.drawable.ic_folder),
            onClick = onGroupClick
        )
        SelectRow(
            label = stringResource(R.string.place_create_tag),
            value = categoryValue,
            leadingIcon = painterResource(R.drawable.ic_tag),
            onClick = onCategoryClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceCreateScreenContentsPreview() {
    MemoripTheme {
        PlaceCreateScreenContents(
            uiState = PlaceCreateUiState(),
            onAction = {}
        )
    }
}