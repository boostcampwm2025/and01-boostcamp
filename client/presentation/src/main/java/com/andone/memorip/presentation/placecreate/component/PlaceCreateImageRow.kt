package com.andone.memorip.presentation.placecreate.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil3.Uri
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun PlaceCreateImageRow(
    selectedImages: List<Uri>,
    maxCount: Int,
    onRemoveImage: (Uri) -> Unit,
    onAddImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
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
            onClick = onAddImageClick,
            modifier = Modifier.padding(vertical = MemoripPadding.PaddingXSmall)
        )
    }
}

@Preview
@Composable
private fun PlaceCreateImageRow(){
    MemoripTheme {
        PlaceCreateImageRow(

        )
    }
}