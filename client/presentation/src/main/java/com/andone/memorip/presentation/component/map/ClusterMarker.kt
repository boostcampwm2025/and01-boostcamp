package com.andone.memorip.presentation.component.map

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.component.map.ClusterMarkerDimen.BorderWidth
import com.andone.memorip.presentation.component.map.ClusterMarkerDimen.CornerRadius
import com.andone.memorip.presentation.component.map.ClusterMarkerDimen.InnerSize
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object ClusterMarkerDimen {
    val InnerSize = 48.dp
    val BorderWidth: Dp = MemoripLineWidth.Small
    val CornerRadius: Dp = 6.dp
}

@Composable
fun ClusterMarker(
    count: Int,
    modifier: Modifier = Modifier,
    imageBitmap: Bitmap? = null
) {
    Box(
        modifier = modifier
            .size(size = InnerSize + BorderWidth * 2)
            .clip(shape = RoundedCornerShape(size = CornerRadius))
            .background(MemoripTheme.colors.background)
    ) {
        Box(
            modifier = Modifier
                .size(InnerSize)
                .clip(shape = RoundedCornerShape(size = CornerRadius - BorderWidth))
                .background(MemoripTheme.colors.primaryContainer)
                .align(Alignment.Center)
        ) {
            imageBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.matchParentSize()
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(all = MemoripPadding.PaddingXXXSmall),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (count > 99) "99+" else count.toString(),
                    color = MemoripTheme.colors.background,
                    style = MemoripTheme.typography.labelMedium14
                )
            }
        }
    }
}

@Preview
@Composable
private fun ClusterMarkerPreview() {
    MemoripTheme {
        ClusterMarker(count = 100, imageBitmap = null)
    }
}