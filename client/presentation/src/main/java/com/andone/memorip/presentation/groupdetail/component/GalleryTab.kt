package com.andone.memorip.presentation.groupdetail.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.MemoripStaggeredGrid
import com.andone.memorip.presentation.groupdetail.model.PlaceImageItem
import com.andone.memorip.presentation.theme.MemoripTheme

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
        val dummyImages = remember {
            List(30) { index ->
                val randomHeight = (150..400).random()
                val fixedWidth = 200
                PlaceImageItem(
                    id = index,
                    url = "https://picsum.photos/id/${index + 1}/$fixedWidth/$randomHeight",
                    width = fixedWidth,
                    height = randomHeight
                )
            }
        }
        GalleryTab(
            images = dummyImages,
            modifier = Modifier.fillMaxSize()
        )
    }
}