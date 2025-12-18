package com.andone.memorip.presentation.groupdetail.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object ImageMarkerDimen {
    val ImageSize: Dp = 48.dp
    val ShadowElevation: Dp = 4.dp
    const val ShadowAlpha: Float = 0.25f
}

@Composable
fun ImageMarker(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val borderColor = MemoripTheme.colors.offWhite

    Box(
        modifier = modifier.size(
            width = ImageMarkerDimen.ImageSize + MemoripPadding.PaddingXXSmall,
            height = ImageMarkerDimen.ImageSize + MemoripPadding.PaddingXXSmall + MemoripPadding.PaddingXSmall
        ),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .size(ImageMarkerDimen.ImageSize + MemoripPadding.PaddingXXSmall)
                .background(
                    color = borderColor,
                    shape = RoundedCornerShape(MemoripPadding.PaddingXSmall)
                )
                .border(
                    width = MemoripPadding.PaddingXXXSmall,
                    color = borderColor,
                    shape = RoundedCornerShape(MemoripPadding.PaddingXSmall)
                ),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .allowHardware(false)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(ImageMarkerDimen.ImageSize)
                    .clip(RoundedCornerShape(MemoripPadding.PaddingXSmall - MemoripPadding.PaddingXXXSmall))
            )
        }

        Canvas(
            modifier = Modifier
                .size(width = MemoripPadding.PaddingSmall, height = MemoripPadding.PaddingXSmall)
                .offset(y = ImageMarkerDimen.ImageSize + MemoripPadding.PaddingXXXSmall)
                .align(Alignment.TopCenter)
        ) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width / 2, size.height)
                lineTo(size.width, 0f)
                close()
            }
            drawPath(
                path = path,
                color = borderColor
            )
        }
    }
}

@Preview
@Composable
private fun ImageMarkerPreview() {
    MemoripTheme {
        ImageMarker(imageUrl = "https://picsum.photos/id/1/200/300")
    }
}