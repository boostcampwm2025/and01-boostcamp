package com.andone.memorip.presentation.screen.selectimage.component

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.andone.memorip.presentation.screen.selectimage.component.CropImageStateHolderConstants.ZOOM_IN_SCALE
import com.andone.memorip.presentation.screen.selectimage.model.CropRatio
import com.andone.memorip.presentation.util.BitmapCropUtil
import com.andone.memorip.presentation.util.loadBitmapFromUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private object CropImageStateHolderConstants {
    const val ZOOM_IN_SCALE = 3
}

@Composable
fun rememberCropImageState(
    imageUris: List<Uri>,
    context: Context,
    onImagesCrop: (List<Uri>) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope()
): CropImageState {
    return remember(imageUris, context, onImagesCrop, scope) {
        CropImageState(imageUris, context, scope, onImagesCrop)
    }
}

@Stable
class CropImageState(
    private val imageUris: List<Uri>,
    private val context: Context,
    private val scope: CoroutineScope,
    private val onImagesCrop: (List<Uri>) -> Unit
) {
    var currentIndex by mutableIntStateOf(0)
        private set

    val currentUri: Uri?
        get() = imageUris.getOrNull(currentIndex)

    var imageBitmap by mutableStateOf<Bitmap?>(null)
        private set

    var scale by mutableFloatStateOf(1f)
        private set

    var offset by mutableStateOf(Offset.Zero)
        private set

    var aspectRatio by mutableStateOf(CropRatio.Vertical)
        private set

    var viewSize by mutableStateOf(Size.Zero)
        private set

    val cropRect by derivedStateOf {
        BitmapCropUtil.calculateCropRect(viewSize, aspectRatio.ratio)
    }

    val croppedImages = mutableStateMapOf<Uri, Uri>()

    fun updateViewSize(size: Size) {
        viewSize = size
        resetTransformation()
    }

    fun updateAspectRatio(ratio: CropRatio) {
        aspectRatio = ratio
        resetTransformation()
    }

    fun selectImage(index: Int) {
        currentIndex = index
    }

    suspend fun loadImage() {
        currentUri?.let { uri ->
            imageBitmap = loadBitmapFromUri(context = context, uri = uri)
            resetTransformation()
        }
    }

    private fun resetTransformation() {
        imageBitmap?.let { bitmap ->
            if (viewSize != Size.Zero) {
                scale = BitmapCropUtil.calculateCenterCropScale(
                    bitmap = bitmap,
                    viewSize = viewSize,
                    cropRect = cropRect
                )
                offset = Offset.Zero
            }
        }
    }

    fun onGesture(
        pan: Offset,
        zoom: Float
    ) {
        imageBitmap?.let { bitmap ->
            val minScale = BitmapCropUtil.calculateMinScale(
                bitmap = bitmap,
                cropRect = cropRect
            )
            val maxScale = minScale * ZOOM_IN_SCALE
            val calculatedScale = (scale * zoom).coerceIn(minScale, maxScale)

            offset = BitmapCropUtil.calculateOffset(
                bitmap = bitmap,
                scale = calculatedScale,
                currentOffset = offset,
                pan = pan,
                cropRect = cropRect
            )
            scale = calculatedScale
        }
    }

    fun cropImage() {
        scope.launch {
            val bitmap = imageBitmap
            val uri = currentUri
            if (bitmap != null && uri != null) {
                val resultUri = BitmapCropUtil.cropImage(
                    context = context,
                    bitmap = bitmap,
                    viewSize = viewSize,
                    offset = offset,
                    scale = scale,
                    cropRect = cropRect
                )
                croppedImages[uri] = resultUri

                if (croppedImages.size == imageUris.size) {
                    val finalResult = imageUris.map { croppedImages[it]!! }
                    onImagesCrop(finalResult)
                } else {
                    val nextIndex = imageUris.indexOfFirst { !croppedImages.containsKey(it) }
                    if (nextIndex != -1) {
                        currentIndex = nextIndex
                    }
                }
            }
        }
    }
}