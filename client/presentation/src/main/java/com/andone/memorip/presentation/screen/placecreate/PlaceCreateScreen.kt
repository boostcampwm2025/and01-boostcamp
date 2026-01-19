package com.andone.memorip.presentation.screen.placecreate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.screen.placecreate.PictureSetting.MAX_PICTURE_COUNT
import com.andone.memorip.presentation.screen.placecreate.component.PlaceCreateContentSection
import com.andone.memorip.presentation.screen.placecreate.component.PlaceCreateImageRow
import com.andone.memorip.presentation.screen.placecreate.component.PlaceCreateSelectSection
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateEvent
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateUiState
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
fun PlaceCreateScreenContent(
    uiState: PlaceCreateUiState,
    onAction: (PlaceCreateAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(MemoripPadding.PaddingXSmall),
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall)
    ) {
        PlaceCreateImageRow(
            selectedImages = uiState.images,
            maxCount = MAX_PICTURE_COUNT,
            onRemoveImage = { uri -> onAction(PlaceCreateAction.OnImagesRemove(uri)) }
        )

        PlaceCreateContentSection(
            title = uiState.title,
            content = uiState.content,
            isPublic = uiState.isPublic,
            onTitleChange = { onAction(PlaceCreateAction.OnTitleChange(it)) },
            onContentChange = { onAction(PlaceCreateAction.OnContentChange(it)) },
            onCheckedChange = { onAction(PlaceCreateAction.OnPublicChange) }
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

@Preview(showBackground = true)
@Composable
private fun PlaceCreateScreenContentsPreview() {
    MemoripTheme {
        PlaceCreateScreenContent(
            uiState = PlaceCreateUiState(),
            onAction = {}
        )
    }
}