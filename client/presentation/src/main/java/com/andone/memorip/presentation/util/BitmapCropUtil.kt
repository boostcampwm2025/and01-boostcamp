package com.andone.memorip.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

object BitmapCropUtil {

    /** 중앙 크롭 박스 **/
    fun calculateCropRect(
        viewSize: Size,
        aspectRatio: Float,
    ): Rect {
        if (viewSize == Size.Zero) return Rect.Zero

        val cropWidth: Float
        val cropHeight: Float

        // 비율에 따른 크롭할 크기
        if (viewSize.width / viewSize.height > aspectRatio) {
            cropHeight = viewSize.height
            cropWidth = cropHeight * aspectRatio
        } else {
            cropWidth = viewSize.width
            cropHeight = cropWidth / aspectRatio
    suspend fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return withContext(Dispatchers.IO) {
            runCatching {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
            }.getOrNull()
        }

        // 크롭 시작 좌표
        val left = (viewSize.width - cropWidth) / 2
        val top = (viewSize.height - cropHeight) / 2

        return Rect(
            left = left,
            top = top,
            right = left + cropWidth,
            bottom = top + cropHeight
        )
    }

    /** 초기 이미지 비율 **/
    fun calculateCenterCropScale(
        bitmap: Bitmap,
        viewSize: Size,
        cropRect: Rect
    ): Float {
        if (bitmap.width == 0 || bitmap.height == 0) return 1f

        // 크롭 박스를 빈틈없이 꽉 채우는 배율
        val scaleToFillCrop = max(
            cropRect.width / bitmap.width,
            cropRect.height / bitmap.height
        )

        // 이미지가 화면 밖으로 나가지 않게 하는 배율
        val scaleToFitView = min(
            viewSize.width / bitmap.width,
            viewSize.height / bitmap.height
        )

        // 둘 중 작은 값을 선택 (화면을 벗어나는 것을 방지하는 것이 우선)
        return min(scaleToFillCrop, scaleToFitView)
    }

    /** 줌 아웃 한계 **/
    fun calculateMinScale(
        bitmap: Bitmap,
        cropRect: Rect
    ): Float {
        if (bitmap.width == 0 || bitmap.height == 0) return 1f

        // 이미지 가로/세로 중 적어도 한 변은 크롭 박스 닿아
        return min(
            cropRect.width / bitmap.width,
            cropRect.height / bitmap.height
        )
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