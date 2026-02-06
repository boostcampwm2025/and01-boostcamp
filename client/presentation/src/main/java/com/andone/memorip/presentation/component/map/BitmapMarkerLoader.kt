package com.andone.memorip.presentation.component.map

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.platform.LocalContext
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.size.Scale
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min
import androidx.core.graphics.scale
import coil3.size.Precision

private object MarkerImageConstant {
    const val SIZE = 200
    val BITMAP_CONFIG = Bitmap.Config.RGB_565
}

@Composable
fun rememberBitmapMarkerLoader(
    imageUrls: List<String>,
    size: Int = MarkerImageConstant.SIZE
): SnapshotStateMap<String, Bitmap> {
    val context = LocalContext.current
    val markerImages = remember { mutableStateMapOf<String, Bitmap>() }

    LaunchedEffect(imageUrls) {
        val targets = imageUrls.filterNot { markerImages.containsKey(it) }

        targets.forEach { url ->
            launch(Dispatchers.IO) {
                runCatching {
                    val request = ImageRequest.Builder(context)
                        .data(url)
                        .size(size)
                        .scale(Scale.FIT)
                        .precision(Precision.INEXACT)
                        .allowHardware(false)
                        .build()

                    val result = context.imageLoader.execute(request)
                    if (result !is SuccessResult) throw IllegalStateException("Load failed")

                    val bitmap = result.image.toBitmap()
                    val configuredBitmap = if (bitmap.config != MarkerImageConstant.BITMAP_CONFIG) {
                        bitmap.copy(MarkerImageConstant.BITMAP_CONFIG, false).also { bitmap.recycle() }
                    } else {
                        bitmap
                    }
                    configuredBitmap.centerSquareCrop(size)
                }.onSuccess { processedBitmap ->
                    withContext(Dispatchers.Main) {
                        markerImages[url] = processedBitmap
                    }
                }
            }
        }
    }
    return markerImages
}

private fun Bitmap.centerSquareCrop(targetSize: Int): Bitmap {
    val minDimension = min(width, height)
    val x = (width - minDimension) / 2
    val y = (height - minDimension) / 2

    val cropped = Bitmap.createBitmap(this, x, y, minDimension, minDimension)
    return if (minDimension != targetSize) {
        cropped.scale(targetSize, targetSize).also {
            if (cropped != it) cropped.recycle()
        }
    } else {
        cropped
    }
}
