package com.andone.memorip.presentation.groupdetail.component

import android.content.res.Configuration
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
    modifier: Modifier = Modifier
) {
    MemoripStaggeredGrid(
        images = images,
        modifier = modifier
    )
}

@Preview
@Composable
private fun GalleryTabPreview() {
    MemoripTheme {
        GalleryTab(
            images = DummyData.dummyImages,
            modifier = Modifier.fillMaxSize()
        )
    }
}