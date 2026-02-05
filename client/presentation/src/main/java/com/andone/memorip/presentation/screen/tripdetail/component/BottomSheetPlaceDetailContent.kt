package com.andone.memorip.presentation.screen.tripdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.toImmutableList

private object BottomSheetPlaceDetailContentDimen {
    val IMAGE_SIZE = 100.dp
    val IMAGE_CORNER_RADIUS = 8.dp
    val BOTTOM_BUTTON_CORNER_RADIUS = 12.dp
    val BUTTON_ICON_SIZE = 20.dp
}

@Composable
fun BottomSheetPlaceDetailContent(
    place: Place,
    onCloseClick: () -> Unit,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = MemoripPadding.AppHorizontalPadding
                ),
            verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = place.name,
                    modifier = Modifier.weight(1f),
                    color = MemoripTheme.colors.onSurface,
                    style = MemoripTheme.typography.headlineBold20
                )

                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.dialog_close_button_description),
                        tint = MemoripTheme.colors.onSurface
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceMedium)
            ) {
                MemoripImage(
                    imageUrl = place.thumbnailImage.url,
                    contentDescription = stringResource(R.string.place_detail_image_content_description),
                    modifier = Modifier
                        .size(BottomSheetPlaceDetailContentDimen.IMAGE_SIZE)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(BottomSheetPlaceDetailContentDimen.IMAGE_CORNER_RADIUS)),
                    contentScale = ContentScale.Crop,
                    targetWidth = BottomSheetPlaceDetailContentDimen.IMAGE_SIZE,
                    targetHeight = BottomSheetPlaceDetailContentDimen.IMAGE_SIZE
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
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
                    TextButton(
                        onClick = onDetailClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(BottomSheetPlaceDetailContentDimen.BOTTOM_BUTTON_CORNER_RADIUS),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MemoripTheme.colors.primary,
                            contentColor = MemoripTheme.colors.white
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.place_detail_bottom_sheet_go_to_detail),
                            style = MemoripTheme.typography.bodyMedium14
                        )
                        Spacer(modifier = Modifier.width(MemoripSpace.SpaceXSmall))
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_forward),
                            contentDescription = null,
                            modifier = Modifier.size(BottomSheetPlaceDetailContentDimen.BUTTON_ICON_SIZE),
                            tint = MemoripTheme.colors.white
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetPlaceDetailContentPreview() {
    MemoripTheme {
        BottomSheetPlaceDetailContent(
            place = DummyData.places.last(),
            onCloseClick = {},
            onDetailClick = {}
        )
    }
}
