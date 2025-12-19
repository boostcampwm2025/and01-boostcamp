package com.andone.memorip.presentation.component

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object ImageMarkerDimen {
    val ImageSize: Dp = 48.dp
}

@Composable
fun ImageMarker(
    imageBitmap: Bitmap,
    modifier: Modifier = Modifier,
    borderColor: Color = MemoripTheme.colors.primaryContainer
) {
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
                .clip(RoundedCornerShape(MemoripPadding.PaddingXSmall))
                .background(borderColor),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = imageBitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(ImageMarkerDimen.ImageSize)
                    .clip(RoundedCornerShape(MemoripPadding.PaddingXSmall - MemoripPadding.PaddingXXXSmall))
                    .background(Color.White)
            )
        }

        // 꼬리
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
        val context = LocalContext.current
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)
        val bitmap = drawable?.toBitmap(200, 200, Bitmap.Config.ARGB_8888)!!
        ImageMarker(imageBitmap = bitmap)
    }
}