package com.andone.memorip.presentation.component.map

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.map.ImageMarkerDimen.BorderWidth
import com.andone.memorip.presentation.component.map.ImageMarkerDimen.CornerRadius
import com.andone.memorip.presentation.component.map.ImageMarkerDimen.ImageSize
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripTheme

private object ImageMarkerDimen {
    val ImageSize: Dp = 48.dp
    val BorderWidth: Dp = MemoripLineWidth.Medium
    val CornerRadius: Dp = 6.dp
}

@Composable
fun ImageMarker(
    imageBitmap: Bitmap,
    modifier: Modifier = Modifier,
    borderColor: Color = MemoripTheme.colors.primaryContainer
) {
    Box(
        modifier = modifier
            .size(size = ImageSize + BorderWidth * 2)
            .clip(shape = RoundedCornerShape(size = CornerRadius))
            .background(borderColor),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = imageBitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier
                .size(ImageSize)
                .clip(shape = RoundedCornerShape(size = CornerRadius - BorderWidth))
                .background(Color.White)
        )
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