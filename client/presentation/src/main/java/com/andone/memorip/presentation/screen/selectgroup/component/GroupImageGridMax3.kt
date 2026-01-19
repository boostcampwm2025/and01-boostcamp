package com.andone.memorip.presentation.screen.selectgroup.component

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.screen.selectgroup.component.GroupImageGridMax3Constants.ASPECT_RATIO_HEIGHT
import com.andone.memorip.presentation.screen.selectgroup.component.GroupImageGridMax3Constants.ASPECT_RATIO_WIDTH
import com.andone.memorip.presentation.screen.selectgroup.component.GroupImageGridMax3Constants.THREE_IMAGES_LARGE_WEIGHT
import com.andone.memorip.presentation.screen.selectgroup.component.GroupImageGridMax3Constants.THREE_IMAGES_SMALL_WEIGHT
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

private object GroupImageGridMax3Constants {
    const val ASPECT_RATIO_WIDTH = 3f
    const val ASPECT_RATIO_HEIGHT = 2f
    const val THREE_IMAGES_LARGE_WEIGHT = 2f
    const val THREE_IMAGES_SMALL_WEIGHT = 1f
}

@Composable
fun GroupImageGridMax3(
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
            text = stringResource(R.string.group_image_grid_empty_message),
            style = MemoripTheme.typography.bodySmall,
            color = MemoripTheme.colors.onSurface
        )
    }
}

@Composable
private fun SingleImageLayout(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    MemoripImage(
        imageUrl = imageUrl,
        contentDescription = null,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun TwoImagesLayout(
    firstImage: String,
    secondImage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)
    ) {
        MemoripImage(
            imageUrl = firstImage,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
        MemoripImage(
            imageUrl = secondImage,
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
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
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)
    ) {
        MemoripImage(
            imageUrl = firstImage,
            contentDescription = null,
            modifier = Modifier
                .weight(THREE_IMAGES_LARGE_WEIGHT)
                .fillMaxHeight()
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
                    .fillMaxWidth()
            )
            MemoripImage(
                imageUrl = thirdImage,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun GroupImageGridMax3EmptyPreview() {
    MemoripTheme {
        GroupImageGridMax3(
            images = emptyList(),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun GroupImageGridMax3SinglePreview() {
    MemoripTheme {
        GroupImageGridMax3(
            images = listOf("https://example.com/image1.jpg"),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun GroupImageGridMax3TwoPreview() {
    MemoripTheme {
        GroupImageGridMax3(
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
private fun GroupImageGridMax3MoreThanThreePreview() {
    MemoripTheme {
        GroupImageGridMax3(
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