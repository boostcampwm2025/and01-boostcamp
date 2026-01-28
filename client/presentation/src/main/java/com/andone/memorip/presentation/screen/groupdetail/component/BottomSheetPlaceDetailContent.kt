package com.andone.memorip.presentation.screen.groupdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.component.PlaceLocationText
import com.andone.memorip.presentation.component.TagChipRow
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.groupdetail.component.BottomSheetPlaceDetailContentDimen.IMAGE_CORNER_RADIUS
import com.andone.memorip.presentation.screen.groupdetail.component.BottomSheetPlaceDetailContentDimen.IMAGE_SIZE
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.toImmutableList

private object BottomSheetPlaceDetailContentDimen {
    val IMAGE_SIZE = 80.dp
    val IMAGE_CORNER_RADIUS = 12.dp
}

@Composable
fun BottomSheetPlaceDetailContent(
    place: Place,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = MemoripPadding.PaddingMedium,
                    vertical = MemoripPadding.PaddingMedium
                ),
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceMedium)
        ) {
            Text(
                text = place.name,
                style = MemoripTheme.typography.headline2,
                color = MemoripTheme.colors.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceMedium)
            ) {
                MemoripImage(
                    imageUrl = place.thumbnailImage.url,
                    contentDescription = stringResource(R.string.place_detail_image_content_description),
                    modifier = Modifier
                        .size(IMAGE_SIZE)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(IMAGE_CORNER_RADIUS)),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall)
                ) {
                    PlaceLocationText(
                        address = place.address,
                        iconTint = MemoripTheme.colors.primary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (place.categories.isNotEmpty()) {
                        TagChipRow(
                            tags = place.categories.toImmutableList(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(MemoripPadding.PaddingSmall),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MemoripTheme.colors.onSurface
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                contentDescription = stringResource(R.string.dialog_close_button_description)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetPlaceDetailContentPreview() {
    MemoripTheme {
        BottomSheetPlaceDetailContent(
            place = DummyData.places.first(),
            onCloseClick = {}
        )
    }
}
