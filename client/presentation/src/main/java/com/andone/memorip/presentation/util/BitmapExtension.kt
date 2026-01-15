package com.andone.memorip.presentation.util

import android.graphics.Bitmap
import android.graphics.Color

fun Bitmap.calculateLuminanceAverage(): Double {
    var sum = 0.0
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)

    this.getPixels(pixels, 0, width, 0, 0, width, height)

    pixels.forEach { pixel ->
        val r = Color.red(pixel)
        val g = Color.green(pixel)
        val b = Color.blue(pixel)
        sum += 0.299 * r + 0.587 * g + 0.114 * b
    }

    return sum / pixels.size
}

fun Bitmap.cropBottomArea(ratio: Float): Bitmap {
    val endY = (this.height * ratio).toInt()
    val startY = this.height - endY
    return Bitmap.createBitmap(
        this,
        0,
        startY,
        this.width,
        endY
    )
}