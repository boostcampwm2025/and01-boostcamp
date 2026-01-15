package com.andone.memorip.presentation.screen.selectimage

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.toSize
import androidx.core.graphics.withSave
import com.andone.memorip.presentation.screen.selectimage.ImageCropScreenDimens.DRAW_RECT_ALPHA
import com.andone.memorip.presentation.screen.selectimage.component.ImageCropRatioButton
import com.andone.memorip.presentation.screen.selectimage.component.ImageCropBottomBar
import com.andone.memorip.presentation.screen.selectimage.component.rememberCropImageState
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripTheme

private object ImageCropScreenDimens {
    const val DRAW_RECT_ALPHA = 0.5f
}

@Composable
fun ImageCropScreen(
    imageUris: List<Uri>,
    onDismiss: () -> Unit,
    onImagesCrop: (List<Uri>) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val state = rememberCropImageState(
        imageUris = imageUris,
        context = context,
        onImagesCrop = onImagesCrop,
    )

    LaunchedEffect(state.currentUri) {
        state.loadImage()
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RectangleShape)
                .onGloballyPositioned { state.updateViewSize(it.size.toSize()) }
                .pointerInput(state.currentUri, state.aspectRatio, state.viewSize) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        state.onGesture(pan, zoom)
                    }
                }
        ) {
            ImageCropSection(
                imageBitmap = state.imageBitmap,
                viewSize = state.viewSize,
                offset = state.offset,
                scale = state.scale,
                cropRect = state.cropRect
            )

            ImageCropRatioButton(
                aspectRatio = state.aspectRatio,
                onRatioSelect = { state.updateAspectRatio(it) },
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

        ImageCropBottomBar(
            imageUris = imageUris,
            croppedImageKeys = state.croppedImages.keys,
            currentIndex = state.currentIndex,
            onDismiss = onDismiss,
            onClickImage = { state.selectImage(it) },
            onImageCrop = state::cropImage
        )
    }
}

@Composable
private fun ImageCropSection(
    imageBitmap: Bitmap?,
    viewSize: Size,
    offset: Offset,
    scale: Float,
    cropRect: Rect
) {
    val drawRectColor = MemoripTheme.colors.primary
    val clipRectColor = MemoripTheme.colors.black

    imageBitmap?.let { bitmap ->
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 이미지 중앙 그리기
            with(drawContext.canvas.nativeCanvas) {
                withSave {
                    translate(
                        viewSize.width / 2 + offset.x,
                        viewSize.height / 2 + offset.y
                    )
                    scale(scale, scale)
                    translate(-bitmap.width / 2f, -bitmap.height / 2f)
                    drawBitmap(bitmap, 0f, 0f, null)
                }
            }

            // Crop 밖 부분 그리기
            clipRect(
                left = cropRect.left,
                top = cropRect.top,
                right = cropRect.right,
                bottom = cropRect.bottom,
                clipOp = ClipOp.Difference
            ) {
                drawRect(clipRectColor.copy(alpha = DRAW_RECT_ALPHA))
            }

            // Crop 부분 테두리 그리기
            drawRect(
                color = drawRectColor,
                topLeft = cropRect.topLeft,
                size = cropRect.size,
                style = Stroke(width = MemoripBorderWidth.Small.toPx())
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageCropScreenPreview() {
    ImageCropScreen(
        imageUris = emptyList(),
        onDismiss = {},
        onImagesCrop = {}
    )
}