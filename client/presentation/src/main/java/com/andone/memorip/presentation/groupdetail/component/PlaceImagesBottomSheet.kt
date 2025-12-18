package com.andone.memorip.presentation.groupdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.model.ImageItem
import com.andone.memorip.presentation.theme.MemoripAlpha
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object PlaceImagesBottomSheetDimens {
    const val ImageAspectRatio = 1f
    val ImageGridMinSize = 100.dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceImagesBottomSheet(
    placeName: String,
    images: List<ImageItem>,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        containerColor = MemoripTheme.colors.offWhite,
    ) {
        PlaceImagesContent(
            placeName = placeName,
            images = images
        )
    }
}

@Composable
private fun PlaceImagesContent(
    placeName: String,
    images: List<ImageItem>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = MemoripPadding.PaddingXLarge),
        verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceMedium),
    ) {
        PlaceImagesHeader(
            placeName = placeName,
            imageCount = images.size
        )
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = PlaceImagesBottomSheetDimens.ImageGridMinSize),
            contentPadding = PaddingValues(horizontal = MemoripPadding.PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall),
            horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall),
        ) {
            items(images) { image ->
                PlaceImageItemCard(imageUrl = image.url)
            }
        }
    }
}

@Composable
private fun PlaceImagesHeader(
    placeName: String,
    imageCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MemoripPadding.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
    ) {
        Text(
            text = placeName,
            color = MemoripTheme.colors.onOffWhite,
            style = MemoripTheme.typography.body1,
        )
        
        Text(
            text = stringResource(R.string.placeimagebottomsheet_place_image_count, imageCount),
            color = MemoripTheme.colors.onOffWhite.copy(alpha = MemoripAlpha.SECONDARY),
            style = MemoripTheme.typography.body2
        )
    }
}

@Composable
private fun PlaceImageItemCard(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(PlaceImagesBottomSheetDimens.ImageAspectRatio)
            .clip(MemoripTheme.shapes.roundedSmall)
            .background(MemoripTheme.colors.offWhite)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            placeholder = ColorPainter(MemoripTheme.colors.offWhite),
            error = ColorPainter(MemoripTheme.colors.offWhite),
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceImagesContentPreview() {
    MemoripTheme {
        PlaceImagesContent(
            placeName = "에펠탑",
            images = DummyData.placeImages
        )
    }
}