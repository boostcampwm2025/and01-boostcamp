package com.andone.memorip.presentation.screen.placeedit

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.LoadingIndicatorScreen
import com.andone.memorip.presentation.screen.placecreate.ContentSection
import com.andone.memorip.presentation.screen.placecreate.ImageRowSection
import com.andone.memorip.presentation.screen.placecreate.PublicCheckSection
import com.andone.memorip.presentation.screen.placecreate.SelectSection
import com.andone.memorip.presentation.screen.placeedit.component.PlaceEditBottomBar
import com.andone.memorip.presentation.screen.placeedit.component.PlaceEditTopBar
import com.andone.memorip.presentation.screen.placeedit.model.PlaceEditAction
import com.andone.memorip.presentation.screen.placeedit.model.PlaceEditEvent
import com.andone.memorip.presentation.screen.placeedit.model.PlaceEditUiState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun PlaceEditScreen(
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onGroupClick: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val initUiState by viewModel.initUiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            PlaceEditEvent.NavigateBack -> onNavigateBack()
            PlaceEditEvent.NavigateToCategory -> onCategoryClick()
            PlaceEditEvent.NavigateToLocation -> onLocationClick()
            PlaceEditEvent.NavigateToGroup -> onGroupClick()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        PlaceEditScreenContent(
            uiState = uiState,
            initUiState = initUiState,
            onAction = viewModel::onAction
        )

        if (uiState.isLoading) {
            LoadingIndicatorScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceEditScreenContent(
    uiState: PlaceEditUiState,
    initUiState: PlaceEditUiState,
    onAction: (PlaceEditAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState(uiState.scrollPosition)

    val placeEditEnable = (uiState.images.isNotEmpty() &&
            uiState.location != null &&
            uiState.title.isNotBlank() &&
            uiState.groups.isNotEmpty()) &&
            uiState != initUiState

    LaunchedEffect(Unit) {
        onAction(PlaceEditAction.OnImageSelect(uiState.images.first()))
    }

    LaunchedEffect(uiState.images) {
        if (uiState.images.isEmpty()) {
            onAction(PlaceEditAction.OnLastImageRemove)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onAction(PlaceEditAction.OnScrollPositionChange(scrollState.value))
        }
    }

    Scaffold(
        topBar = {
            PlaceEditTopBar(
                value = stringResource(R.string.place_edit_title),
                onBackClick = { onAction(PlaceEditAction.OnBackClick) }
            )
        },
        bottomBar = {
            PlaceEditBottomBar(
                value = stringResource(R.string.place_edit_title),
                onClick = { },
                enabled = placeEditEnable
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
                onImageSelect = { uri -> onAction(PlaceEditAction.OnImageSelect(uri)) },
                onRemoveImage = { uri -> onAction(PlaceEditAction.OnImagesRemove(uri)) }
            )

            ContentSection(
                title = uiState.title,
                content = uiState.content,
                onTitleChange = { onAction(PlaceEditAction.OnTitleChange(it)) },
                onContentChange = { onAction(PlaceEditAction.OnContentChange(it)) },
            )

            SelectSection(
                category = uiState.tags,
                location = uiState.location,
                groups = uiState.groups,
                onCategoryClick = { onAction(PlaceEditAction.OnCategoryClick) },
                onLocationClick = { onAction(PlaceEditAction.OnLocationClick) },
                onGroupClick = { onAction(PlaceEditAction.OnGroupClick) },
            )

            PublicCheckSection(
                isPublic = uiState.isPublic,
                onCheckedChange = { onAction(PlaceEditAction.OnPublicChange) }
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlaceEditScreenPreview() {
    MemoripTheme {
        PlaceEditContainer(
            place = DummyData.place,
            onNavigateBack = {}
        )
    }
}