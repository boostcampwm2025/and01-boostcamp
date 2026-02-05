package com.andone.memorip.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun MemoripImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = ColorPainter(MemoripTheme.colors.primaryContainer),
        // todo: error, fallback 이미지 대체 필요
        error = ColorPainter(MemoripTheme.colors.primaryContainer),
        fallback = ColorPainter(MemoripTheme.colors.primaryContainer),
        contentScale = contentScale
    )
}