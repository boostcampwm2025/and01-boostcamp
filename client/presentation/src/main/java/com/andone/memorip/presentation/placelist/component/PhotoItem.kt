package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun PhotoItem(place: Place) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(place.thumbnailImage.aspectRatio)
            .background(color = MemoripTheme.colors.primaryContainer)
    ) {
        AsyncImage(
            model = place.thumbnailImage.url.takeIf { it.isNotBlank() },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun PhotoItemPreview(){
    MemoripTheme {
        PhotoItem(
            place = Place.empty()
        )
    }
}