package com.andone.memorip.presentation.screen.placelist.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.component.PlaceLocationText
import com.andone.memorip.presentation.screen.placelist.component.StaggeredGridDimens.OVERLAY_HEIGHT
import com.andone.memorip.presentation.screen.placelist.component.StaggeredGridDimens.STAGGERED_GRID_IMAGE_CORNER_RADIUS
import com.andone.memorip.presentation.screen.placelist.component.StaggeredImageItemConstant.BOTTOM_ALPHA
import com.andone.memorip.presentation.screen.placelist.component.StaggeredImageItemConstant.BOTTOM_RATIO
import com.andone.memorip.presentation.screen.placelist.component.StaggeredImageItemConstant.MIDDLE_ALPHA
import com.andone.memorip.presentation.screen.placelist.component.StaggeredImageItemConstant.MIDDLE_RATIO
import com.andone.memorip.presentation.screen.placelist.component.StaggeredImageItemConstant.TOP_ALPHA
import com.andone.memorip.presentation.screen.placelist.component.StaggeredImageItemConstant.TOP_RATIO
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object StaggeredGridDimens {
    val STAGGERED_GRID_IMAGE_CORNER_RADIUS = 16.dp
    val OVERLAY_HEIGHT = 56.dp
}

private object StaggeredImageItemConstant{
    val TOP_ALPHA = 0f
    val MIDDLE_ALPHA = 0.8f
    val BOTTOM_ALPHA = 1f
    val TOP_RATIO = 0f
    val MIDDLE_RATIO = 0.5f
    val BOTTOM_RATIO = 1f
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun StaggeredImageItem(
    imageUrl: String,
    aspectRatio: Float,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = STAGGERED_GRID_IMAGE_CORNER_RADIUS,
    contentDescription: String? = null,
    location: String? = null
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val imageHeight = screenWidth / aspectRatio

    Box(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onImageClick
            )
            .fillMaxWidth()
            .clip(RoundedCornerShape(cornerRadius))
            .background(MemoripTheme.colors.gray)
    ) {
        MemoripImage(
            imageUrl = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio),
            targetWidth = screenWidth,
            targetHeight = imageHeight
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(OVERLAY_HEIGHT)
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            TOP_RATIO to MemoripTheme.colors.background.copy(alpha = TOP_ALPHA),
                            MIDDLE_RATIO to MemoripTheme.colors.background.copy(alpha = MIDDLE_ALPHA),
                            BOTTOM_RATIO to MemoripTheme.colors.background.copy(alpha = BOTTOM_ALPHA)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = MemoripPadding.PaddingXSmall, vertical = MemoripPadding.PaddingXXSmall),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = contentDescription?.takeIf { it.isNotBlank() }
                    ?: stringResource(R.string.place_list_no_title),
                style = MemoripTheme.typography.bodyBold14,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MemoripTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(MemoripPadding.PaddingXXSmall))

            PlaceLocationText(
                address = location?.takeIf { it.isNotBlank() }
                    ?: stringResource(R.string.place_list_no_address_title)
            )
        }
    }
}

@Preview
@Composable
private fun StaggeredImageItemPreview() {
    MemoripTheme {
        StaggeredImageItem(
            imageUrl = "https://picsum.photos/id/1/200/300",
            aspectRatio = 1f,
            onImageClick = {},
        )
    }
}