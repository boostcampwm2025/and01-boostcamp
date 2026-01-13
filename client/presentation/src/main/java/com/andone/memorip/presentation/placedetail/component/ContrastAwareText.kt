package com.andone.memorip.presentation.placedetail.component

import android.graphics.Bitmap
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.placedetail.component.Constants.cropRate
import com.andone.memorip.presentation.placedetail.component.Constants.luminusThreshold
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.calculateLuminusAverage
import com.andone.memorip.presentation.util.cropBottomArea

private object Constants {
    val cropRate = 0.2f
    val luminusThreshold = 128
}

@Composable
fun ContrastAwareText(
    image: Bitmap?,
    text: String,
    style: TextStyle
) {
    val textColor = if (image != null) {
        val luminusValue = image.cropBottomArea(ratio = cropRate)
            .calculateLuminusAverage()
        if (luminusValue > luminusThreshold) MemoripTheme.colors.black
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