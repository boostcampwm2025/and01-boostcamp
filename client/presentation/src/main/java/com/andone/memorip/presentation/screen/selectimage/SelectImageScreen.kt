package com.andone.memorip.presentation.screen.selectimage

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.screen.selectimage.SelectImageScreenConstants.IMAGE_PICKER_MIN_COUNT
import com.andone.memorip.presentation.screen.selectimage.SelectImageScreenConstants.MAX_PICTURE_COUNT
import com.andone.memorip.presentation.screen.selectimage.model.SelectImageAction
import com.andone.memorip.presentation.screen.selectimage.model.SelectImageEvent
import com.andone.memorip.presentation.screen.selectimage.model.SelectImageUiState
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle
import kotlin.math.max

private object SelectImageScreenConstants {
    const val MAX_PICTURE_COUNT = 10
    const val IMAGE_PICKER_MIN_COUNT = 2
}

@Composable
fun SelectImageScreen(
    onBack: () -> Unit,
    onImageSelect: (List<Uri>, Float) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SelectImageViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            SelectImageEvent.NavigateBack -> {
                onBack()
            }

            is SelectImageEvent.NavigateToSelectLocation -> {
                onImageSelect(event.images, event.thumbnailImageRatio)
            }
        }
    }

    SelectImageScreenContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun SelectImageScreenContent(
    uiState: SelectImageUiState,
    onAction: (SelectImageAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val imageCount = max(MAX_PICTURE_COUNT - uiState.selectedImages.size, IMAGE_PICKER_MIN_COUNT)
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = imageCount)
    ) { uris ->
        if (uris.isNotEmpty()) {
            onAction(SelectImageAction.OnImagesSelect(uris.take(imageCount)))
        } else {
            onAction(SelectImageAction.OnBack)
        }
    }

    LaunchedEffect(Unit) {
        if (uiState.selectedImages.isEmpty()) {
            imagePickerLauncher.launch(
                input = PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    if (uiState.selectedImages.isNotEmpty()) {
        ImageCropScreen(
            imageUris = uiState.selectedImages,
            transformData = uiState.transformData,
            onImagesCrop = { uris, data -> onAction(SelectImageAction.OnImagesCrop(uris, data)) },
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SelectImageScreenPreview() {
    MemoripTheme {
        SelectImageScreen(onBack = {}, onImageSelect = { _, _ -> })
    }
}