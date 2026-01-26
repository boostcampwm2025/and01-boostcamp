package com.andone.memorip.presentation.theme

import androidx.compose.ui.unit.dp

object MemoripPadding {
    val AppHorizontalPadding = 16.dp
    val PaddingXXXSmall = 2.dp
    val PaddingXXSmall = 4.dp
    val PaddingXSmall = 8.dp
    val PaddingSmall = 12.dp
    val PaddingMedium = 16.dp
    val PaddingLarge = 20.dp
    val PaddingXLarge = 24.dp
    val PaddingXXXLarge = 32.dp
}

object MemoripSpace {
    val SpaceXXSmall = 4.dp
    val SpaceXSmall = 8.dp
    val SpaceSmall = 12.dp
    val SpaceMedium = 16.dp
    val SpaceLarge = 20.dp
    val SpaceXLarge = 24.dp
    val SpaceXXXLarge = 32.dp
}

object MemoripIconSize {
    val IconSizeXXSmall = 12.dp
    val IconSizeXSmall = 16.dp
    val IconSizeSmall = 20.dp
    val IconSizeMedium = 24.dp
    val IconSizeLarge = 40.dp
}

object MemoripHeight {
    val bottomBar = 60.dp
    val TextBoxHigh = 120.dp
    val SearchBoxHeight = 36.dp
}

object MemoripLineWidth {

    val Hairline = 0.5.dp
    val Thin = 1.dp
    val Small = 2.dp

    val Medium = 4.dp
    val Strong = 8.dp

    val TimeMajor = 1.2.dp
    val TimeMinor = 0.7.dp
    val TimeTick = 0.4.dp
}

object MemoripShadow {
    val Small = 1.dp
    val Medium = 4.dp
    val Large = 8.dp
}

object MemoripAlpha {
    /* ---------- Disabled / Inactive ---------- */
    const val DISABLED = 0.38f
    const val INACTIVE = 0.6f
    const val SECONDARY = 0.7f

    const val DEFAULT = 1f


    /* ---------- Overlay ---------- */
    const val SCRIM = 0.5f            // 모달/다이얼로그 배경
    const val DIM = 0.4f              // 이미지 위 딤 처리


    /* ---------- Press / Focus ---------- */
    const val PRESSED = 0.8f
    const val HOVER = 0.9f


    /* ---------- Image ---------- */
    const val IMAGE_PLACEHOLDER = 0.3f
    const val IMAGE_OVERLAY = 0.5f


    /* ---------- Divider / Border ---------- */
    const val DIVIDER = 0.12f
    const val BORDER = 0.2f

    /* ---------- Icon / IconButton ---------- */
    const val BUTTON = 0.6f
}

object MemoripElevation {
    val ElevationMedium = 4.dp
}

object MemoripDragConstants {
    val DRAG_SCALE = 1.05f
    val DRAG_SHADOW_ELEVATION = 12f
    val DRAG_Z_INDEX = 1f
}