package com.andone.memorip.presentation.screen.selectimage.component

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.andone.memorip.presentation.screen.selectimage.component.ImageCropStateHolderConstants.MAX_RATIO
import com.andone.memorip.presentation.screen.selectimage.component.ImageCropStateHolderConstants.MIN_SIZE
import com.andone.memorip.presentation.util.BitmapCropUtil
import com.andone.memorip.presentation.util.BitmapCropUtil.bitmapToScreenRect
import com.andone.memorip.presentation.util.BitmapCropUtil.calculateOffset
import com.andone.memorip.presentation.util.BitmapCropUtil.loadBitmapFromUri
import com.andone.memorip.presentation.util.BitmapCropUtil.screenToBitmapRect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private object ImageCropStateHolderConstants {
    const val MIN_SIZE = 300f
    const val MAX_RATIO = 4f
}

enum class DragHandle {
    None,
    Left, Right, Top, Bottom,
    TopLeft, TopRight, BottomLeft, BottomRight
}

@Composable
fun rememberCropImageState(
    imageUris: List<Uri>,
    context: Context,
    cropPadding: Float,
    onImagesCrop: (List<Uri>) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope()
): CropImageState {
    return remember(imageUris, context, cropPadding, scope, onImagesCrop) {
        CropImageState(imageUris, context, cropPadding, scope, onImagesCrop)
    }
}

@Stable
class CropImageState(
    private val imageUris: List<Uri>,
    private val context: Context,
    private val cropPadding: Float,
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

    var viewSize by mutableStateOf(Size.Zero)
        private set

    var cropRect by mutableStateOf(Rect.Zero)
        private set

    val croppedImages = mutableStateMapOf<Uri, Uri>()

    private var currentDragHandle by mutableStateOf(DragHandle.None)

    private val viewCenter: Offset
        get() = Offset(viewSize.width / 2, viewSize.height / 2)

    private val availableViewSize: Size
        get() = Size(
            max(1f, viewSize.width - (cropPadding * 2)),
            max(1f, viewSize.height - (cropPadding * 2))
        )

    fun updateViewSize(size: Size) {
        viewSize = size
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
        val bitmap = imageBitmap ?: return

        if (viewSize != Size.Zero) {
            scale = min(
                availableViewSize.width / bitmap.width,
                availableViewSize.height / bitmap.height
            )
            offset = Offset.Zero

            val imageWidth = bitmap.width * scale
            val imageHeight = bitmap.height * scale

            val left = (viewSize.width - imageWidth) / 2
            val top = (viewSize.height - imageHeight) / 2

            cropRect = Rect(left, top, left + imageWidth, top + imageHeight)
        }
    }

    fun dragStart(offset: Offset, touchTarget: Float) {
        currentDragHandle = getHandleAt(offset, touchTarget)
    }

    fun drag(dragOffset: Offset) {
        val bitmap = imageBitmap ?: return

        if (currentDragHandle != DragHandle.None) {
            handleResize(dragOffset, bitmap)
        } else {
            // 이미지 이동
            offset = calculateOffset(
                bitmap = bitmap,
                scale = scale,
                currentOffset = offset,
                pan = dragOffset,
                cropRect = cropRect
            )
        }
    }

    fun dragEnd() {
        if (currentDragHandle != DragHandle.None) {
            moveCropRectToCenter()
        }
        currentDragHandle = DragHandle.None
    }

    private fun getHandleAt(offset: Offset, touchTarget: Float): DragHandle {
        // 모서리 우선 체크
        if ((offset - cropRect.topLeft).getDistance() <= touchTarget) return DragHandle.TopLeft
        if ((offset - cropRect.topRight).getDistance() <= touchTarget) return DragHandle.TopRight
        if ((offset - cropRect.bottomLeft).getDistance() <= touchTarget) return DragHandle.BottomLeft
        if ((offset - cropRect.bottomRight).getDistance() <= touchTarget) return DragHandle.BottomRight

        // 변 체크
        if (abs(offset.x - cropRect.left) <= touchTarget && offset.y >= cropRect.top && offset.y <= cropRect.bottom) return DragHandle.Left
        if (abs(offset.x - cropRect.right) <= touchTarget && offset.y >= cropRect.top && offset.y <= cropRect.bottom) return DragHandle.Right
        if (abs(offset.y - cropRect.top) <= touchTarget && offset.x >= cropRect.left && offset.x <= cropRect.right) return DragHandle.Top
        if (abs(offset.y - cropRect.bottom) <= touchTarget && offset.x >= cropRect.left && offset.x <= cropRect.right) return DragHandle.Bottom

        return DragHandle.None
    }

    private fun handleResize(dragOffset: Offset, bitmap: Bitmap) {
        val dragOffsetX = dragOffset.x / scale
        val dragOffsetY = dragOffset.y / scale

        val bitmapRect = screenToBitmapRect(viewCenter + offset, cropRect, bitmap, scale)
        var newLeft = bitmapRect.left
        var newTop = bitmapRect.top
        var newRight = bitmapRect.right
        var newBottom = bitmapRect.bottom

        val imageHeight = newBottom - newTop
        val imageWidth = newRight - newLeft

        // 최대 비율을 넘지 않게 보정
        fun correctCornerRatio() {
            val maxImageHeight = imageHeight * MAX_RATIO
            val maxImageWidth = imageWidth * MAX_RATIO

            if (imageWidth > maxImageHeight) {
                if (currentDragHandle == DragHandle.TopLeft || currentDragHandle == DragHandle.BottomLeft) {
                    newLeft = newRight - maxImageHeight
                } else {
                    newRight = newLeft + maxImageHeight
                }
            } else if (imageHeight > maxImageWidth) {
                if (currentDragHandle == DragHandle.TopLeft || currentDragHandle == DragHandle.TopRight) {
                    newTop = newBottom - maxImageWidth
                } else {
                    newBottom = newTop + maxImageWidth
                }
            }
        }

        // 핸들 타입에 따른 이동 및 제한
        when (currentDragHandle) {
            DragHandle.Left -> {
                val range = getValidRange(imageHeight)
                newLeft = (newLeft + dragOffsetX).coerceIn(
                    newRight - range.endInclusive,
                    newRight - range.start
                )
            }

            DragHandle.Right -> {
                val range = getValidRange(imageHeight)
                newRight = (newRight + dragOffsetX).coerceIn(
                    newLeft + range.start,
                    newLeft + range.endInclusive
                )
            }

            DragHandle.Top -> {
                val range = getValidRange(imageWidth)
                newTop = (newTop + dragOffsetY).coerceIn(
                    newBottom - range.endInclusive,
                    newBottom - range.start
                )
            }

            DragHandle.Bottom -> {
                val range = getValidRange(imageWidth)
                newBottom = (newBottom + dragOffsetY).coerceIn(
                    newTop + range.start,
                    newTop + range.endInclusive
                )
            }

            DragHandle.TopLeft -> {
                newLeft = min(newLeft + dragOffsetX, newRight - MIN_SIZE)
                newTop = min(newTop + dragOffsetY, newBottom - MIN_SIZE)
                correctCornerRatio()
            }

            DragHandle.TopRight -> {
                newRight = max(newRight + dragOffsetX, newLeft + MIN_SIZE)
                newTop = min(newTop + dragOffsetY, newBottom - MIN_SIZE)
                correctCornerRatio()
            }

            DragHandle.BottomLeft -> {
                newLeft = min(newLeft + dragOffsetX, newRight - MIN_SIZE)
                newBottom = max(newBottom + dragOffsetY, newTop + MIN_SIZE)
                correctCornerRatio()
            }

            DragHandle.BottomRight -> {
                newRight = max(newRight + dragOffsetX, newLeft + MIN_SIZE)
                newBottom = max(newBottom + dragOffsetY, newTop + MIN_SIZE)
                correctCornerRatio()
            }

            DragHandle.None -> {}
        }

        // 비트맵 경계 내로 Clamp
        newLeft = newLeft.coerceIn(0f, bitmap.width.toFloat())
        newTop = newTop.coerceIn(0f, bitmap.height.toFloat())
        newRight = newRight.coerceIn(0f, bitmap.width.toFloat())
        newBottom = newBottom.coerceIn(0f, bitmap.height.toFloat())

        // 화면에 Fit
        fitBitmapRectToScreen(Rect(newLeft, newTop, newRight, newBottom), bitmap)
    }

    private fun getValidRange(size: Float): ClosedFloatingPointRange<Float> {
        val minSize = max(MIN_SIZE, size / MAX_RATIO)
        val maxSize = size * MAX_RATIO
        return minSize..maxSize
    }

    private fun fitBitmapRectToScreen(bitmapRect: Rect, bitmap: Bitmap) {
        val newScale = min(
            scale,
            min(
                availableViewSize.width / bitmapRect.width,
                availableViewSize.height / bitmapRect.height
            )
        )
        var newOffsetX = offset.x
        var newOffsetY = offset.y

        if (newScale < scale) {
            // 줌 아웃 발생 시 중앙 정렬
            newOffsetX = (bitmap.width / 2f - bitmapRect.center.x) * newScale
            newOffsetY = (bitmap.height / 2f - bitmapRect.center.y) * newScale
        } else {
            // 크롭 박스 경계 보정
            val screenRect = bitmapToScreenRect(viewCenter + offset, bitmapRect, bitmap, newScale)

            val minX = cropPadding
            val maxX = viewSize.width - cropPadding
            val minY = cropPadding
            val maxY = viewSize.height - cropPadding

            if (screenRect.left < minX) newOffsetX += minX - screenRect.left
            if (screenRect.right > maxX) newOffsetX += maxX - screenRect.right
            if (screenRect.top < minY) newOffsetY += minY - screenRect.top
            if (screenRect.bottom > maxY) newOffsetY += maxY - screenRect.bottom
        }

        scale = newScale
        offset = Offset(newOffsetX, newOffsetY)
        cropRect = bitmapToScreenRect(viewCenter + offset, bitmapRect, bitmap, newScale)
    }

    private fun moveCropRectToCenter() {
        val zoomScale = min(
            availableViewSize.width / cropRect.width,
            availableViewSize.height / cropRect.height
        )

        scale *= zoomScale
        offset = Offset(
            (viewCenter.x + offset.x - cropRect.center.x) * zoomScale,
            (viewCenter.y + offset.y - cropRect.center.y) * zoomScale
        )

        val cropWidth = cropRect.width * zoomScale
        val cropHeight = cropRect.height * zoomScale

        val left = (viewSize.width - cropWidth) / 2
        val top = (viewSize.height - cropHeight) / 2

        cropRect = Rect(left, top, left + cropWidth, top + cropHeight)
    }

    fun zoom(pan: Offset, zoom: Float) {
        val bitmap = imageBitmap ?: return
        if (currentDragHandle != DragHandle.None) return

        val newScale = (scale * zoom).coerceIn(
            max(cropRect.width / bitmap.width, cropRect.height / bitmap.height),
            MAX_RATIO
        )
        val newOffset = calculateOffset(
            bitmap = bitmap,
            scale = newScale,
            currentOffset = offset,
            pan = pan,
            cropRect = cropRect
        )

        scale = newScale
        offset = newOffset
    }

    fun cropImage() {
        scope.launch {
            val bitmap = imageBitmap
            val uri = currentUri
            if (bitmap != null && uri != null) {
                if (croppedImages.size == imageUris.size) {
                    val finalResult = imageUris.mapNotNull { croppedImages[it] }
                    if (finalResult.size == imageUris.size) {
                        onImagesCrop(finalResult)
                    }
                } else {
                    val resultUri = BitmapCropUtil.cropImage(
                        context = context,
                        bitmap = bitmap,
                        viewSize = viewSize,
                        offset = offset,
                        scale = scale,
                        cropRect = cropRect
                    )
                    croppedImages[uri] = resultUri

                    val nextIndex = imageUris.indexOfFirst { !croppedImages.containsKey(it) }
                    if (nextIndex != -1) {
                        currentIndex = nextIndex
                    }
                }
            }
        }
    }

    fun removeImage() {
        croppedImages.remove(currentUri)
    }
}