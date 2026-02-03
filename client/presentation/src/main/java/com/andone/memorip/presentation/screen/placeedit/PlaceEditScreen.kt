package com.andone.memorip.presentation.screen.placeedit

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.platform.LocalContext
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
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun PlaceEditScreen(
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onTripClick: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isUpdateEnabled by viewModel.isUpdateEnabled.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            PlaceEditEvent.NavigateBack -> onNavigateBack()
            PlaceEditEvent.NavigateToCategory -> onCategoryClick()
            PlaceEditEvent.NavigateToLocation -> onLocationClick()
            PlaceEditEvent.NavigateToTrip -> onTripClick()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        PlaceEditScreenContent(
            uiState = uiState,
            isUpdateEnabled = isUpdateEnabled,
            onAction = viewModel::onAction
        )

        if (uiState.isLoading) {
            LoadingIndicatorScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
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
private fun PlaceEditScreenContent(
    uiState: PlaceEditUiState,
    isUpdateEnabled: Boolean,
    onAction: (PlaceEditAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val scrollState = rememberScrollState(uiState.scrollPosition)

    LaunchedEffect(Unit) {
        onAction(PlaceEditAction.OnImageSelect(uiState.images.first()))
    }

    DisposableEffect(Unit) {
        onDispose {
            onAction(PlaceEditAction.OnScrollPositionChange(scrollState.value))
        }
    }

    Scaffold(
        modifier = modifier.navigationBarsPadding(),
        topBar = {
            PlaceEditTopBar(
                value = stringResource(R.string.place_edit_title),
                onBackClick = { onAction(PlaceEditAction.OnBackClick) }
            )
        },
        bottomBar = {
            PlaceEditBottomBar(
                value = stringResource(R.string.place_edit_title),
                onClick = { onAction(PlaceEditAction.OnPlaceUpdate(context)) },
                enabled = isUpdateEnabled
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
                trips = uiState.trips,
                onCategoryClick = { onAction(PlaceEditAction.OnCategoryClick) },
                onLocationClick = { onAction(PlaceEditAction.OnLocationClick) },
                onTripClick = { onAction(PlaceEditAction.OnTripClick) },
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
        PlaceEditScreenContent(
            uiState = PlaceEditUiState(),
            isUpdateEnabled = true,
            onAction = {}
        )
    }
}