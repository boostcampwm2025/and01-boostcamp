package com.andone.memorip.presentation.screen.selectimage.model

enum class CropRatio(
    val ratioString: String,
    val ratio: Float
) {
    Vertical(
        ratioString = "9:16",
        ratio = 9 / 16f
    ),
    Horizontal(
        ratioString = "16:9",
        ratio = 16 / 9f
    )
}