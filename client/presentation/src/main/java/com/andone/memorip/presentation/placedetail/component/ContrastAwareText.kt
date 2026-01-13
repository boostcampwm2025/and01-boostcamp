package com.andone.memorip.presentation.placedetail.component

import android.R.attr.height
import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.placedetail.component.Constants.cropRate
import com.andone.memorip.presentation.theme.MemoripTheme

private object Constants {
    val cropRate = 0.2f
}

@Composable
fun ContrastAwareText(
    image: Bitmap?,
    text: String,
    style: TextStyle
) {
    val textColor = if (image != null) {
        val cropImage = cropTopArea(bitmap = image)
        val luminusValue = calculateLuminusAverage(bitmap = cropImage)
        if (luminusValue > 128) MemoripTheme.colors.black
        else MemoripTheme.colors.white
    } else {
        MemoripTheme.colors.white
    }

    Text(
        text = text,
        style = style,
        color = textColor
    )
}

private fun calculateLuminusAverage(bitmap: Bitmap): Double {
    var sum = 0.0
    val width = bitmap.width
    val height = bitmap.height
    val pixels = IntArray(width * height)

    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

    pixels.forEach { pixel ->
        val r = Color.red(pixel)
        val g = Color.green(pixel)
        val b = Color.blue(pixel)
        sum += 0.299 * r + 0.587 * g + 0.114 * b
    }

    return sum / pixels.size
}

private fun cropTopArea(bitmap: Bitmap, ratio: Float = cropRate): Bitmap {
    val endY = (bitmap.height * ratio).toInt()
    val startY = bitmap.height - endY
    return Bitmap.createBitmap(
        bitmap,
        0,
        startY,
        bitmap.width,
        endY
    )
}

@Preview
@Composable
fun ContrastAwareTextPrev() {
    MemoripTheme {
        ContrastAwareText(
            image = null,
            text = "",
            style = MemoripTheme.typography.label1
        )
    }
}