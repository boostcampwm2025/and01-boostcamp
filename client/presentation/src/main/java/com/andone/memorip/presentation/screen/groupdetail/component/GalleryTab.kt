package com.andone.memorip.presentation.screen.groupdetail.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.MemoripStaggeredGrid
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

@Composable
fun GalleryTab(
    places: List<Place>,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    MemoripStaggeredGrid(
        places = places,
        onImageClick = onImageClick,
        modifier = modifier
    )
}

@Preview
@Composable
private fun GalleryTabPreview() {
    MemoripTheme {
        GalleryTab(
            places = DummyData.places,
            onImageClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}