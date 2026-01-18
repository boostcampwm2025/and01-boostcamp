package com.andone.memorip.presentation.screen.placedetail.component

import android.graphics.Bitmap
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.screen.placedetail.component.Constants.bottomCropRate
import com.andone.memorip.presentation.screen.placedetail.component.Constants.textColorLuminanceThreshold
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.calculateLuminanceAverage
import com.andone.memorip.presentation.util.cropBottomArea

private object Constants {
    val bottomCropRate = 0.2f
    val textColorLuminanceThreshold = 128
}

@Composable
fun ContrastAwareText(
    image: Bitmap?,
    text: String,
    style: TextStyle
) {
    val textColor = if (image != null) {
        val luminance = image.cropBottomArea(ratio = bottomCropRate)
            .calculateLuminanceAverage()
        if (luminance > textColorLuminanceThreshold) MemoripTheme.colors.black
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