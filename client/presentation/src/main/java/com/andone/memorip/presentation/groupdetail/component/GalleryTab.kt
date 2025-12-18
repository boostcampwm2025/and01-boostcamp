package com.andone.memorip.presentation.groupdetail.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.MemoripStaggeredGrid
import com.andone.memorip.presentation.groupdetail.model.PlaceImageItem
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

@Composable
fun GalleryTab(
    images: List<PlaceImageItem>,
    onImageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    MemoripStaggeredGrid(
        images = images,
        onImageClick = onImageClick,
        modifier = modifier
    )
}

@Preview
@Composable
private fun GalleryTabPreview() {
    MemoripTheme {
        GalleryTab(
            images = DummyData.dummyImages,
            onImageClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}