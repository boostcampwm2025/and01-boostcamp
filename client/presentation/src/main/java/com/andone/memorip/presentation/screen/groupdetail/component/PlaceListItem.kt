package com.andone.memorip.presentation.screen.groupdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
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

private object PlaceListItemDimen {
    val IMAGE_SIZE = 80.dp
    val IMAGE_CORNER_RADIUS = 8.dp
    val CARD_ELEVATION = 2.dp
}

@Composable
fun PlaceListItem(
    place: Place,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = { onClick(place.id) },
        modifier = modifier.fillMaxWidth(),
        shape = MemoripTheme.shapes.roundedMedium,
        color = MemoripTheme.colors.white,
        tonalElevation = PlaceListItemDimen.CARD_ELEVATION
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = MemoripPadding.PaddingMedium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MemoripImage(
                    imageUrl = place.thumbnailImage.url,
                    contentDescription = place.name,
                    modifier = Modifier
                        .size(PlaceListItemDimen.IMAGE_SIZE)
                        .clip(shape = RoundedCornerShape(size = PlaceListItemDimen.IMAGE_CORNER_RADIUS)),
                    contentScale = ContentScale.Crop
                )

                Column(verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall)) {
                    Text(
                        text = place.name,
                        color = MemoripTheme.colors.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MemoripTheme.typography.bodyLarge
                    )
                    PlaceLocationText(
                        address = place.address,
                        iconTint = MemoripTheme.colors.primary
                    )
                    TagChipRow(tags = place.categories.toImmutableList())
                }
            }

            Icon(
                painter = painterResource(R.drawable.ic_chevron_forward),
                contentDescription = null,
                tint = MemoripTheme.colors.gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceListItemPreview() {
    MemoripTheme {
        PlaceListItem(
            place = DummyData.places.first(),
            onClick = {}
        )
    }
}