package com.andone.memorip.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Precision
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.toPx

@Composable
fun MemoripImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    targetWidth: Dp? = null,
    targetHeight: Dp? = null
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val widthPx = targetWidth?.toPx(density)?.toInt()
    val heightPx = targetHeight?.toPx(density)?.toInt()

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .apply {
                if (widthPx != null && heightPx != null) {
                    size(widthPx, heightPx)
                }
            }
            .precision(Precision.INEXACT)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = ColorPainter(MemoripTheme.colors.primaryContainer),
        // todo: error, fallback 이미지 대체 필요
        error = ColorPainter(MemoripTheme.colors.primaryContainer),
        fallback = ColorPainter(MemoripTheme.colors.primaryContainer),
        contentScale = contentScale
    )
}
