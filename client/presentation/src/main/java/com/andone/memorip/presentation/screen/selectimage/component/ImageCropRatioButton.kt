package com.andone.memorip.presentation.screen.selectimage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.screen.selectimage.component.CropRatioButtonDimens.COLOR_ALPHA
import com.andone.memorip.presentation.screen.selectimage.model.CropRatio
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

private object CropRatioButtonDimens {
    const val COLOR_ALPHA = 0.5f
}

@Composable
fun ImageCropRatioButton(
    aspectRatio: CropRatio,
    onRatioSelect: (CropRatio) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(top = MemoripPadding.PaddingLarge)
            .background(MemoripTheme.colors.black.copy(COLOR_ALPHA), CircleShape)
            .padding(MemoripPadding.PaddingXSmall),
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceMedium)
    ) {
        RatioButton(
            ratio = CropRatio.Vertical,
            aspectRatio = aspectRatio,
            onClick = { onRatioSelect(CropRatio.Vertical) }
        )
        RatioButton(
            ratio = CropRatio.Horizontal,
            aspectRatio = aspectRatio,
            onClick = { onRatioSelect(CropRatio.Horizontal) }
        )
    }
}

@Composable
private fun RatioButton(
    ratio: CropRatio,
    aspectRatio: CropRatio,
    onClick: (CropRatio) -> Unit
) {
    val color = if (ratio == aspectRatio) MemoripTheme.colors.yellow else MemoripTheme.colors.white

    TextButton(onClick = { onClick(ratio) }) {
        Text(
            text = ratio.ratioString,
            color = color
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ImageCropRatioButtonPreview() {
    ImageCropRatioButton(
        aspectRatio = CropRatio.Vertical,
        onRatioSelect = {},
    )
}