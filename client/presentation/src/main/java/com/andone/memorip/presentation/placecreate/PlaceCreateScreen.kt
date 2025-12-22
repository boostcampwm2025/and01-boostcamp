package com.andone.memorip.presentation.placecreate

import android.net.Uri
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.placecreate.PictureSetting.MAX_PICTURE_COUNT
import com.andone.memorip.presentation.placecreate.component.PlaceCreateContentSection
import com.andone.memorip.presentation.placecreate.component.PlaceCreateImageRow
import com.andone.memorip.presentation.placecreate.component.PlaceCreateSelectSection
import com.andone.memorip.presentation.placecreate.component.PlaceCreateTopBar
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
            PlaceCreateTopBar(
                onBackClick = onBackClick,
                onConfirmClick = { },
                confirmEnabled = false
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
                selectedImages = selectedImages,
                maxCount = MAX_PICTURE_COUNT,
                onRemoveImage = { selectedImages.remove(it) },
                onAddImageClick = {
                    if (remain > 0) {
                        imagePickerLauncher.launch(
                            input = PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                }
            )

            PlaceCreateContentSection(
                title = title,
                content = content,
                onTitleChange = { title = it },
                onContentChange = { content = it }
            )

            PlaceCreateSelectSection(
                onCategoryClick = onCategoryClick,
                onLocationClick = onLocationClick,
                onGroupClick = onGroupClick
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