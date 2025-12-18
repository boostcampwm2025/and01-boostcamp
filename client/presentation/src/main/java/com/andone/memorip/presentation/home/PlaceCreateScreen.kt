package com.andone.memorip.presentation.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Tag
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripInputBox
import com.andone.memorip.presentation.home.PictureSetting.MAX_PICTURE_COUNT
import com.andone.memorip.presentation.home.component.ImageCountButton
import com.andone.memorip.presentation.home.component.SelectRow
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import org.andone.memorip.presentation.theme.MemoripTypography

private object PictureSetting{
    const val MAX_PICTURE_COUNT = 10
}

@Composable
fun PlaceCreateScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    PlaceCreateScreenContents(
        modifier = modifier,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceCreateScreenContents(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val selectedImages = remember { mutableStateListOf<Uri>() }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(
                maxItems = MAX_PICTURE_COUNT
            )
        ) { uris ->
            if (uris.isNotEmpty()) {
                selectedImages.addAll(uris)
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.place_create_back_content_description)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}, enabled = false) {
                        Icon(
                            imageVector = Icons.Filled.Check,
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
            ImageCountButton(
                current = selectedImages.size,
                max = MAX_PICTURE_COUNT,
                onClick = {
                    imagePickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                modifier = Modifier.padding(vertical = MemoripPadding.PaddingXSmall)
            )
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
                leadingIcon = Icons.Outlined.Tag,
                onClick = { }
            )
            SelectRow(
                label = stringResource(R.string.place_create_location),
                value = "",
                leadingIcon = Icons.Outlined.Place,
                onClick = { }
            )
            SelectRow(
                label = stringResource(R.string.place_create_group),
                value = "",
                leadingIcon = Icons.Outlined.Folder,
                onClick = { }
            )
        }
    }
}

@Preview
@Composable
private fun PlaceCreateScreenContentsPreview() {
    MemoripTheme {
        PlaceCreateScreenContents(
            onBackClick = {}
        )
    }
}