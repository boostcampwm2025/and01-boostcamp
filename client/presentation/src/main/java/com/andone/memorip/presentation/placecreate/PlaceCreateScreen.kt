package com.andone.memorip.presentation.placecreate

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.placecreate.PictureSetting.MAX_PICTURE_COUNT
import com.andone.memorip.presentation.placecreate.component.PlaceCreateContentSection
import com.andone.memorip.presentation.placecreate.component.PlaceCreateImageRow
import com.andone.memorip.presentation.placecreate.component.PlaceCreateSelectSection
import com.andone.memorip.presentation.placecreate.component.PlaceCreateTopBar
import com.andone.memorip.presentation.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.placecreate.model.PlaceCreateEvent
import com.andone.memorip.presentation.placecreate.model.PlaceCreateUiState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle

private object PictureSetting {
    const val MAX_PICTURE_COUNT = 10
}

@Composable
fun PlaceCreateScreen(
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onGroupClick: () -> Unit,
    onSnackBarShow: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            PlaceCreateEvent.NavigateBack -> onBackClick()
            PlaceCreateEvent.NavigateToCategory -> onCategoryClick()
            PlaceCreateEvent.NavigateToLocation -> onLocationClick()
            PlaceCreateEvent.NavigateToGroup -> onGroupClick()
            PlaceCreateEvent.ShowSnackBar -> onSnackBarShow(R.string.place_create_snack_bar_input_message)
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
    val placeCreateEnable =
        uiState.images.isNotEmpty() && uiState.title.isNotBlank() && uiState.location != null && uiState.group != null
    val remainImageCount = MAX_PICTURE_COUNT - uiState.images.size

    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            val canAdd = MAX_PICTURE_COUNT - uiState.images.size
            if (uris.isNotEmpty()) {
                onAction(PlaceCreateAction.OnImagesAdd(uris.take(canAdd)))
            }
        }

    Scaffold(
        modifier = modifier,
        topBar = {
            PlaceCreateTopBar(
                onBackClick = { onAction(PlaceCreateAction.OnBackClick) },
                onConfirmClick = {
                    if (placeCreateEnable) {
                        onAction(PlaceCreateAction.OnPlaceCreate)
                    } else {
                        onAction(PlaceCreateAction.OnSnackBarShow)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(MemoripPadding.PaddingXSmall),
            verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall)
        ) {
            PlaceCreateImageRow(
                selectedImages = uiState.images,
                maxCount = MAX_PICTURE_COUNT,
                onRemoveImage = { uri -> onAction(PlaceCreateAction.OnImagesRemove(uri)) },
                onAddImageClick = {
                    if (remainImageCount > 0) {
                        imagePickerLauncher.launch(
                            input = PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                }
            )

            PlaceCreateContentSection(
                title = uiState.title,
                content = uiState.content,
                onTitleChange = { onAction(PlaceCreateAction.OnTitleChange(it)) },
                onContentChange = { onAction(PlaceCreateAction.OnContentChange(it)) }
            )

            PlaceCreateSelectSection(
                category = uiState.category,
                location = uiState.location,
                group = uiState.group,
                onCategoryClick = { onAction(PlaceCreateAction.OnCategoryClick) },
                onLocationClick = { onAction(PlaceCreateAction.OnLocationClick) },
                onGroupClick = { onAction(PlaceCreateAction.OnGroupClick) },
                modifier = Modifier.padding(bottom = MemoripPadding.PaddingMedium)
            )
        }
    }
}

@Preview
@Composable
private fun PlaceCreateScreenContentsPreview() {
    MemoripTheme {
        PlaceCreateScreenContents(
            uiState = PlaceCreateUiState(),
            onAction = {}
        )
    }
}