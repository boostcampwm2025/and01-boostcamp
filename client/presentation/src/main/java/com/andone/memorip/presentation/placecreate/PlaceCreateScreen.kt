package com.andone.memorip.presentation.placecreate

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import com.andone.memorip.presentation.component.MemoripInputBox
import com.andone.memorip.presentation.placecreate.PictureSetting.MAX_PICTURE_COUNT
import com.andone.memorip.presentation.placecreate.component.ImageCountButton
import com.andone.memorip.presentation.placecreate.component.SelectRow
import com.andone.memorip.presentation.placecreate.component.SelectedImageItem
import com.andone.memorip.presentation.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.placecreate.model.PlaceCreateEvent
import com.andone.memorip.presentation.placecreate.model.PlaceCreateUiState
import com.andone.memorip.presentation.theme.MemoripHeight
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
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlaceCreateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            PlaceCreateEvent.NavigateBack -> onBackClick()
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
    val remain = MAX_PICTURE_COUNT - uiState.images.size

    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            val canAdd = MAX_PICTURE_COUNT - uiState.images.size
            if (uris.isNotEmpty()) {
                onAction(PlaceCreateAction.OnAddImages(uris.take(canAdd)))
            }
        }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.place_create_title)) },
                navigationIcon = {
                    IconButton(onClick = { onAction(PlaceCreateAction.OnBackClick) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_arrow_back),
                            contentDescription = stringResource(R.string.place_create_back_content_description)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}, enabled = false) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = stringResource(R.string.place_create_check_content_description)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MemoripTheme.colors.offWhite)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(MemoripPadding.PaddingXSmall),
            verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall)
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                uiState.images.forEach { uri ->
                    SelectedImageItem(
                        imageUri = uri,
                        onRemoveClick = { onAction(PlaceCreateAction.OnRemoveImages(uri)) }
                    )
                }
                ImageCountButton(
                    current = uiState.images.size,
                    max = MAX_PICTURE_COUNT,
                    onClick = {
                        if (remain > 0) {
                            imagePickerLauncher.launch(input = PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    },
                    modifier = Modifier.padding(vertical = MemoripPadding.PaddingXSmall)
                )
            }

            Text(
                text = stringResource(R.string.place_create_content_title),
                style = MemoripTheme.typography.title1
            )
            MemoripInputBox(
                label = stringResource(R.string.place_create_content_title),
                value = uiState.title,
                placeholder = stringResource(R.string.place_create_title_input),
                onValueChange = { onAction(PlaceCreateAction.OnTitleChange(it)) },
                onClear = { onAction(PlaceCreateAction.OnTitleChange("")) },
                height = MemoripHeight.TextBoxDefault
            )

            Text(
                text = stringResource(R.string.place_create_content),
                style = MemoripTheme.typography.title1
            )
            MemoripInputBox(
                label = stringResource(R.string.place_create_content),
                value = uiState.content,
                placeholder = stringResource(R.string.place_create_content_input),
                onValueChange = { onAction(PlaceCreateAction.OnContentChange(it)) },
                onClear = { onAction(PlaceCreateAction.OnContentChange("")) }
            )

            SelectRow(
                label = stringResource(R.string.place_create_category),
                value = "",
                leadingIcon = painterResource(R.drawable.ic_tag),
                onClick = { onAction(PlaceCreateAction.OnCategoryClick) }
            )
            SelectRow(
                label = stringResource(R.string.place_create_location),
                value = "",
                leadingIcon = painterResource(R.drawable.ic_location_on),
                onClick = { onAction(PlaceCreateAction.OnLocationClick) }
            )
            SelectRow(
                label = stringResource(R.string.place_create_group),
                value = "",
                leadingIcon = painterResource(R.drawable.ic_folder),
                onClick = { onAction(PlaceCreateAction.OnGroupClick) }
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