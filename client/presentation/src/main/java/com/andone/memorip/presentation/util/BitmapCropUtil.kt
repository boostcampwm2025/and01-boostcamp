package com.andone.memorip.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.core.graphics.createBitmap
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.roundToInt

object BitmapCropUtil {

    suspend fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return withContext(Dispatchers.IO) {
            runCatching {
                // EXIF 데이터 - 회전 각도 확인
                val rotation = context.contentResolver.openInputStream(uri)?.use { stream ->
                    val orientation = ExifInterface(stream).getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                        else -> 0f
                    }
                } ?: return@runCatching null

                val bitmap = context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                } ?: return@runCatching null

                if (rotation != 0f) {
                    val matrix = Matrix().apply { postRotate(rotation) }
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                } else {
                    bitmap
                }
            }.getOrNull()
        }
    }

    fun bitmapToScreenRect(
        center: Offset,
        bitmapRect: Rect,
        bitmap: Bitmap,
        scale: Float
    ): Rect {
        val left = center.x + (bitmapRect.left - bitmap.width / 2) * scale
        val top = center.y + (bitmapRect.top - bitmap.height / 2) * scale
        val right = center.x + (bitmapRect.right - bitmap.width / 2) * scale
        val bottom = center.y + (bitmapRect.bottom - bitmap.height / 2) * scale
        return Rect(left, top, right, bottom)
    }

    fun screenToBitmapRect(
        center: Offset,
        screenRect: Rect,
        bitmap: Bitmap,
        scale: Float
    ): Rect {
        val left = bitmap.width / 2 + (screenRect.left - center.x) / scale
        val top = bitmap.height / 2 + (screenRect.top - center.y) / scale
        val right = bitmap.width / 2 + (screenRect.right - center.x) / scale
        val bottom = bitmap.height / 2 + (screenRect.bottom - center.y) / scale
        return Rect(left, top, right, bottom)
    }

    /** 터치 감지하여 이미지를 움직이거나 줌인/줌아웃 **/
    fun Modifier.detectEditorGestures(
        key: String,
        onDragStart: (Offset) -> Unit,
        onDrag: (Offset) -> Unit,
        onZoom: (pan: Offset, zoom: Float) -> Unit,
        onDragEnd: () -> Unit
    ): Modifier {
        return pointerInput(key) {
            awaitEachGesture {
                onDragStart(awaitFirstDown().position)

                do {
                    val event = awaitPointerEvent()
                    val calculatedPan = event.calculatePan()
                    val calculatedZoom = event.calculateZoom()

                    // 화면 터치 포인트 개수에 따라 분리
                    if (event.changes.size == 1) {
                        onDrag(calculatedPan)
                    } else {
                        onZoom(calculatedPan, calculatedZoom)
                    }

                    // 화면 터치 포인트 전부 소비
                    event.changes.forEach {
                        if (it.positionChanged()) it.consume()
                    }

                    // 화면 터치 포인트 있으면 걔속 do
                } while (event.changes.any { it.pressed })

                onDragEnd()
            }
        }
    }

    /** 이동 제한 계산 **/
    fun calculateOffset(
        bitmap: Bitmap,
        scale: Float,
        currentOffset: Offset,
        pan: Offset,
        cropRect: Rect
    ): Offset {
        // 이미지가 크롭 박스보다 넘치는 만큼 이동 가능
        val maxOffsetX = max(0f, (bitmap.width * scale - cropRect.width) / 2f)
        val maxOffsetY = max(0f, (bitmap.height * scale - cropRect.height) / 2f)

        val newOffset = currentOffset + pan
        return Offset(
            newOffset.x.coerceIn(-maxOffsetX, maxOffsetX),
            newOffset.y.coerceIn(-maxOffsetY, maxOffsetY)
        )
    }

    /** 이미지 자르기 **/
    suspend fun cropImage(
        context: Context,
        bitmap: Bitmap,
        viewSize: Size,
        offset: Offset,
        scale: Float,
        cropRect: Rect
    ): Uri = withContext(Dispatchers.IO) {

        // 현재 이미지 중점 좌표
        val imageCenterX = viewSize.width / 2 + offset.x
        val imageCenterY = viewSize.height / 2 + offset.y

        // 크롭할 이미지 시작점 좌표
        val startX = bitmap.width / 2 + (cropRect.left - imageCenterX) / scale
        val startY = bitmap.height / 2 + (cropRect.top - imageCenterY) / scale

        // 크롭될 이미지 크기
        val realCropWidth = (cropRect.width / scale).roundToInt()
        val realCropHeight = (cropRect.height / scale).roundToInt()

        // 크롭 크기로 비트맵 생성 및 그리기
        val resultBitmap = createBitmap(
            width = max(1, realCropWidth),
            height = max(1, realCropHeight)
        )
        val canvas = Canvas(resultBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, -startX, -startY, Paint(Paint.FILTER_BITMAP_FLAG))

        val file = File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }

        Uri.fromFile(file)
    }
}