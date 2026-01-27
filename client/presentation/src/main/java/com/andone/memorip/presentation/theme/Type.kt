package com.andone.memorip.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.andone.memorip.presentation.R

val NotoSansKR = FontFamily(
    Font(R.font.noto_sans_kr_black, FontWeight.Black),
    Font(R.font.noto_sans_kr_bold, FontWeight.Bold),
    Font(R.font.noto_sans_kr_extra_bold, FontWeight.ExtraBold),
    Font(R.font.noto_sans_kr_extra_light, FontWeight.ExtraLight),
    Font(R.font.noto_sans_kr_light, FontWeight.Light),
    Font(R.font.noto_sans_kr_medium, FontWeight.Medium),
    Font(R.font.noto_sans_kr_regular, FontWeight.Normal),
    Font(R.font.noto_sans_kr_semi_bold, FontWeight.SemiBold),
    Font(R.font.noto_sans_kr_thin, FontWeight.Thin),
)

@Immutable
data class MemoripTypography(
    val headlineBold32: TextStyle,
    val headlineBold24: TextStyle,
    val headlineBold20: TextStyle,
    val headlineBold18: TextStyle,
    val titleBold20: TextStyle,
    val titleBold18: TextStyle,
    val titleBold16: TextStyle,
    val titleBold14: TextStyle,
    val titleBold12: TextStyle,
    val titleMedium14: TextStyle,
    val bodyBold18: TextStyle,
    val bodyBold16: TextStyle,
    val bodyBold14: TextStyle,
    val bodyBold12: TextStyle,
    val bodyMedium16: TextStyle,
    val bodyMedium14: TextStyle,
    val bodyRegular18: TextStyle,
    val bodyRegular12: TextStyle,
    val bodyRegular10: TextStyle,
    val labelBold16: TextStyle,
    val labelRegular14: TextStyle,
    val labelRegular12: TextStyle,
    val labelRegular10: TextStyle,
    val labelMedium16: TextStyle,
    val labelMedium14: TextStyle,
)

internal val memoripTypography = MemoripTypography(
    headlineBold32 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 48.sp
    ),
    headlineBold24 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    headlineBold20 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    headlineBold18 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleBold20 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    titleBold18 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleBold16 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    titleBold14 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    titleBold12 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    titleMedium14 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodyBold18 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    bodyBold16 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyBold14 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    bodyBold12 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
    ),
    bodyMedium16 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyMedium14 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodyRegular18 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodyRegular12 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    bodyRegular10 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    labelBold16 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    labelRegular14 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelRegular12 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelRegular10 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    labelMedium16 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    labelMedium14 = TextStyle(
        fontFamily = NotoSansKR,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)

internal val LocalMemoripTypography = staticCompositionLocalOf {
    memoripTypography
}