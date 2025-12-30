package com.andone.memorip.presentation.plan.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.component.TagChip
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.selectcategory.model.Category
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.formatTimeRange
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDateTime

private object PlaceTimeCardDimen {
    val COMPACT_IMAGE_SIZE = 80.dp
    val EXPANDED_IMAGE_HEIGHT = 160.dp
    val CARD_ELEVATION = 2.dp
    val IMAGE_CORNER_RADIUS = 8.dp
    const val EXPANDED_LAYOUT_THRESHOLD_MINUTES = 90
}

@Composable
fun PlaceTimeCard(
    place: Place,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (place.durationMinutes < PlaceTimeCardDimen.EXPANDED_LAYOUT_THRESHOLD_MINUTES) {
        CompactPlaceTimeCard(
            place = place,
            onClick = onClick,
            modifier = modifier
        )
    } else {
        ExpandedPlaceTimeCard(
            place = place,
            onClick = onClick,
            modifier = modifier
        )
    }
}

@Composable
private fun CompactPlaceTimeCard(
    place: Place,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MemoripTheme.shapes.roundedMedium,
        color = MemoripTheme.colors.white,
        tonalElevation = PlaceTimeCardDimen.CARD_ELEVATION
    ) {
        Row(
            modifier = Modifier.padding(MemoripPadding.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall)
        ) {
            MemoripImage(
                imageUrl = place.thumbnailImage.url,
                contentDescription = place.name,
                modifier = Modifier
                    .size(PlaceTimeCardDimen.COMPACT_IMAGE_SIZE)
                    .clip(RoundedCornerShape(PlaceTimeCardDimen.IMAGE_CORNER_RADIUS)),
                contentScale = ContentScale.Crop
            )

            Column(verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXXSmall)) {
                Text(
                    text = place.name,
                    color = MemoripTheme.colors.black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MemoripTheme.typography.headline2
                )
                TagChipRow(tags = place.categories.toImmutableList())
                PlaceLocationText(address = place.address)
                PlaceTimeText(
                    startDateTime = place.startDateTime,
                    endDateTime = place.endDateTime
                )
            }
        }
    }
}

@Composable
private fun ExpandedPlaceTimeCard(
    place: Place,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MemoripTheme.shapes.roundedMedium,
        color = MemoripTheme.colors.white,
        tonalElevation = PlaceTimeCardDimen.CARD_ELEVATION
    ) {
        Column {
            MemoripImage(
                imageUrl = place.thumbnailImage.url,
                contentDescription = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = PlaceTimeCardDimen.EXPANDED_IMAGE_HEIGHT)
                    .clip(shape = RoundedCornerShape(size = PlaceTimeCardDimen.IMAGE_CORNER_RADIUS)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(all = MemoripPadding.PaddingMedium),
                verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall)
            ) {
                Text(
                    text = place.name,
                    color = MemoripTheme.colors.black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MemoripTheme.typography.headline2,
                )
                TagChipRow(tags = place.categories.toImmutableList())
                Row(horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall)) {
                    PlaceLocationText(address = place.address)
                    PlaceTimeText(
                        startDateTime = place.startDateTime,
                        endDateTime = place.endDateTime
                    )
                }
            }
        }
    }
}

@Composable
private fun TagChipRow(
    tags: ImmutableList<Category>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
    ) {
        tags.forEach { tag ->
            TagChip(tag = tag)
        }
    }
}

@Composable
private fun PlaceLocationText(
    address: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_location_on),
            contentDescription = stringResource(R.string.plan_location_content_description),
            modifier = Modifier.size(MemoripIconSize.IconSizeSmall),
            tint = MemoripTheme.colors.secondary
        )
        Text(
            text = address,
            color = MemoripTheme.colors.black,
            style = MemoripTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun PlaceTimeText(
    startDateTime: LocalDateTime,
    endDateTime: LocalDateTime,
    modifier: Modifier = Modifier
) {
    Text(
        text = formatTimeRange(startDateTime, endDateTime),
        color = MemoripTheme.colors.gray,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = MemoripTheme.typography.bodySmall,
    )
}

@Preview(showBackground = true)
@Composable
private fun TagChipRowPreview() {
    MemoripTheme {
        TagChipRow(tags = DummyData.categories.toImmutableList())
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceLocationTextPreview() {
    MemoripTheme {
        PlaceLocationText(address = "서울특별시 강남구")
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceTimeTextPreview() {
    MemoripTheme {
        PlaceTimeText(
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.now().plusHours(1)
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlaceTimeCardPreview() {
    MemoripTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Compact: 30분
            PlaceTimeCard(
                place = DummyData.places[0],
                onClick = {}
            )
            // Compact: 60분
            PlaceTimeCard(
                place = DummyData.places[5],
                onClick = {}
            )
            // Expanded: 120분
            PlaceTimeCard(
                place = DummyData.places[4],
                onClick = {}
            )
            // Expanded: 240분
            PlaceTimeCard(
                place = DummyData.places[1],
                onClick = {}
            )
        }
    }
}