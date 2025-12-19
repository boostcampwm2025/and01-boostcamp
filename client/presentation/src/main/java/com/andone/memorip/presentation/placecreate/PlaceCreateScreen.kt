package com.andone.memorip.presentation.placecreate

import android.net.Uri
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripInputBox
import com.andone.memorip.presentation.placecreate.PictureSetting.MAX_PICTURE_COUNT
import com.andone.memorip.presentation.placecreate.component.ImageCountButton
import com.andone.memorip.presentation.placecreate.component.SelectRow
import com.andone.memorip.presentation.placecreate.component.SelectedImageItem
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

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
) {
    PlaceCreateScreenContents(
        onCategoryClick = onCategoryClick,
        onLocationClick = onLocationClick,
        onGroupClick = onGroupClick,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceCreateScreenContents(
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onGroupClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val selectedImages = remember { mutableStateListOf<Uri>() }
    val remain = MAX_PICTURE_COUNT - selectedImages.size

    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            val canAdd = MAX_PICTURE_COUNT - selectedImages.size
            if (uris.isNotEmpty()) {
                selectedImages.addAll(elements = uris.take(n = canAdd))
            }
        }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.place_create_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
                selectedImages.forEach { uri ->
                    SelectedImageItem(
                        imageUri = uri,
                        onRemoveClick = { selectedImages.remove(uri) }
                    )
                }
                ImageCountButton(
                    current = selectedImages.size,
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
                value = title,
                placeholder = stringResource(R.string.place_create_title_input),
                onValueChange = { title = it },
                onClear = { title = "" },
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
                onValueChange = { content = it },
                onClear = { content = "" }
            )

            SelectRow(
                label = stringResource(R.string.place_create_category),
                value = "",
                leadingIcon = painterResource(R.drawable.ic_tag),
                onClick = onCategoryClick
            )
            SelectRow(
                label = stringResource(R.string.place_create_location),
                value = "",
                leadingIcon = painterResource(R.drawable.ic_location_on),
                onClick = onLocationClick
            )
            SelectRow(
                label = stringResource(R.string.place_create_group),
                value = "",
                leadingIcon = painterResource(R.drawable.ic_folder),
                onClick = onGroupClick
            )
        }
    }
}

@Preview
@Composable
private fun PlaceCreateScreenContentsPreview() {
    MemoripTheme {
        PlaceCreateScreenContents(
            onCategoryClick = {},
            onLocationClick = {},
            onGroupClick = {},
            onBackClick = {}
        )
    }
}