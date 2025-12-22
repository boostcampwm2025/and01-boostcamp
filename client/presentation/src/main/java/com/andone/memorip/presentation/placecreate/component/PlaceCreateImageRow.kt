package com.andone.memorip.presentation.placecreate.component

import android.net.Uri
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import androidx.core.net.toUri

@Composable
fun PlaceCreateImageRow(
    selectedImages: List<Uri>,
    maxCount: Int,
    onRemoveImage: (Uri) -> Unit,
    onAddImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(state = rememberScrollState()),
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

@Preview(showBackground = true)
@Composable
private fun PlaceCreateImageRowPreview() {
    val previewUris = listOf(
        "content://preview/image1".toUri(),
        "content://preview/image2".toUri(),
        "content://preview/image3".toUri()
    )

    MemoripTheme {
        PlaceCreateImageRow(
            selectedImages = previewUris,
            maxCount = 10,
            onRemoveImage = {},
            onAddImageClick = {}
        )
    }
}