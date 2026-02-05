package com.andone.memorip.presentation.screen.selecttrip.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.screen.selecttrip.component.TripImageGridMax3Constants.ASPECT_RATIO_HEIGHT
import com.andone.memorip.presentation.screen.selecttrip.component.TripImageGridMax3Constants.ASPECT_RATIO_WIDTH
import com.andone.memorip.presentation.screen.selecttrip.component.TripImageGridMax3Constants.THREE_IMAGES_LARGE_WEIGHT
import com.andone.memorip.presentation.screen.selecttrip.component.TripImageGridMax3Constants.THREE_IMAGES_SMALL_WEIGHT
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

private object TripImageGridMax3Constants {
    const val ASPECT_RATIO_WIDTH = 3f
    const val ASPECT_RATIO_HEIGHT = 2f
    const val THREE_IMAGES_LARGE_WEIGHT = 2f
    const val THREE_IMAGES_SMALL_WEIGHT = 1f
}

@Composable
fun TripImageGridMax3(
    images: List<String>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT)
            .clip(memoripShapes.roundedSmall)
            .clickable(onClick = onClick)
    ) {
        when {
            images.isEmpty() -> EmptyImageState()
            images.size == 1 -> SingleImageLayout(images[0])
            images.size == 2 -> TwoImagesLayout(images[0], images[1])
            else -> ThreeImagesLayout(images[0], images[1], images[2])
        }
    }
}

@Composable
private fun EmptyImageState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MemoripTheme.colors.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.trip_image_grid_empty_message),
            style = MemoripTheme.typography.bodyBold14,
            color = MemoripTheme.colors.onSurface
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun SingleImageLayout(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val containerHeight = screenWidth * ASPECT_RATIO_HEIGHT / ASPECT_RATIO_WIDTH

    MemoripImage(
        imageUrl = imageUrl,
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        targetWidth = screenWidth,
        targetHeight = containerHeight
    )
}

@Composable
private fun TwoImagesLayout(
    firstImage: String,
    secondImage: String,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val containerHeight = screenWidth * ASPECT_RATIO_HEIGHT / ASPECT_RATIO_WIDTH
    val imageWidth = screenWidth / 2

    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)
    ) {
        MemoripImage(
            imageUrl = firstImage,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            targetWidth = imageWidth,
            targetHeight = containerHeight
        )
        MemoripImage(
            imageUrl = secondImage,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            targetWidth = imageWidth,
            targetHeight = containerHeight
        )
    }
}

@Composable
private fun ThreeImagesLayout(
    firstImage: String,
    secondImage: String,
    thirdImage: String,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val containerHeight = screenWidth * ASPECT_RATIO_HEIGHT / ASPECT_RATIO_WIDTH
    val largeImageWidth = screenWidth * THREE_IMAGES_LARGE_WEIGHT / (THREE_IMAGES_LARGE_WEIGHT + THREE_IMAGES_SMALL_WEIGHT)
    val smallImageWidth = screenWidth * THREE_IMAGES_SMALL_WEIGHT / (THREE_IMAGES_LARGE_WEIGHT + THREE_IMAGES_SMALL_WEIGHT)
    val smallImageHeight = containerHeight / 2

    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)
    ) {
        MemoripImage(
            imageUrl = firstImage,
            contentDescription = null,
            modifier = Modifier
                .weight(THREE_IMAGES_LARGE_WEIGHT)
                .fillMaxHeight(),
            targetWidth = largeImageWidth,
            targetHeight = containerHeight
        )

        Column(
            modifier = Modifier
                .weight(THREE_IMAGES_SMALL_WEIGHT)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)
        ) {
            MemoripImage(
                imageUrl = secondImage,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                targetWidth = smallImageWidth,
                targetHeight = smallImageHeight
            )
            MemoripImage(
                imageUrl = thirdImage,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                targetWidth = smallImageWidth,
                targetHeight = smallImageHeight
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TripImageGridMax3EmptyPreview() {
    MemoripTheme {
        TripImageGridMax3(
            images = emptyList(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TripImageGridMax3SinglePreview() {
    MemoripTheme {
        TripImageGridMax3(
            images = listOf("https://example.com/image1.jpg"),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TripImageGridMax3TwoPreview() {
    MemoripTheme {
        TripImageGridMax3(
            images = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg"
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun TripImageGridMax3MoreThanThreePreview() {
    MemoripTheme {
        TripImageGridMax3(
            images = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg",
                "https://example.com/image4.jpg",
                "https://example.com/image5.jpg"
            ),
            onClick = {}
        )
    }
}