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
import coil3.size.Precision
import coil3.size.Scale
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private object MarkerImageConstant {
    const val WIDTH = 200
    const val HEIGHT = 200
    val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
}

@Composable
fun rememberBitmapMarkerLoader(
    imageUrls: List<String>,
    width: Int = MarkerImageConstant.WIDTH,
    height: Int = MarkerImageConstant.HEIGHT
): SnapshotStateMap<String, Bitmap> {
    val context = LocalContext.current
    val markerImages = remember { mutableStateMapOf<String, Bitmap>() }

    LaunchedEffect(imageUrls) {
        imageUrls.forEach { imageUrl ->
            if (markerImages.containsKey(imageUrl)) return@forEach

            launch(Dispatchers.IO) {
                runCatching {
                    val imageLoader = context.imageLoader
                    val request = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .size(width, height)
                        .precision(Precision.INEXACT)
                        .scale(Scale.FILL)
                        .allowHardware(false)
                        .build()

                    val result = imageLoader.execute(request)
                    if (result is SuccessResult) {
                        result.image.toBitmap(
                            width = width,
                            height = height,
                            config = MarkerImageConstant.BITMAP_CONFIG
                        )
                    } else {
                        error("이미지 로드 실패")
                    }
                }.onSuccess { bitmap ->
                    withContext(Dispatchers.Main) {
                        markerImages[imageUrl] = bitmap
                    }
                }
            }
        }
    }
    return markerImages
}