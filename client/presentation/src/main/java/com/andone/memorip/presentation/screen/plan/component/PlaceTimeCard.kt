package com.andone.memorip.presentation.screen.plan.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.component.PlaceLocationText
import com.andone.memorip.presentation.component.PlaceTimeText
import com.andone.memorip.presentation.component.TagChipRow
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.toImmutableList

private object PlaceTimeCardDimen {
    val CARD_ELEVATION = 2.dp
    val IMAGE_CORNER_RADIUS = 8.dp
}

private object PlaceTimeCardConstants {
    const val COMPACT_LAYOUT_THRESHOLD_MINUTES = 120
    const val EXPANDED_LAYOUT_THRESHOLD_MINUTES = 240
}

@Composable
fun PlaceTimeCard(
    place: Place,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        place.durationMinutes < PlaceTimeCardConstants.COMPACT_LAYOUT_THRESHOLD_MINUTES -> {
            TextPlaceTimeCard(
                place = place,
                onClick = onClick,
                modifier = modifier
            )
        }

        place.durationMinutes < PlaceTimeCardConstants.EXPANDED_LAYOUT_THRESHOLD_MINUTES -> {
            CompactPlaceTimeCard(
                place = place,
                onClick = onClick,
                modifier = modifier
            )
        }

        else -> {
            ExpandedPlaceTimeCard(
                place = place,
                onClick = onClick,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun TextPlaceTimeCard(
    place: Place,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MemoripTheme.shapes.roundedMedium,
        color = MemoripTheme.colors.primaryContainer,
        tonalElevation = PlaceTimeCardDimen.CARD_ELEVATION,
        contentColor = MemoripTheme.colors.onSurface,
    ) {
        Row(
            modifier = Modifier.padding(MemoripPadding.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_location_on),
                contentDescription = null,
                modifier = Modifier.size(MemoripIconSize.IconSizeMedium)
            )
            Text(
                text = place.name,
                modifier = Modifier.alignByBaseline(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MemoripTheme.typography.title1,
            )
            Text(
                text = place.address,
                modifier = Modifier.alignByBaseline(),
                style = MemoripTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis,
            )
        }
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
        color = MemoripTheme.colors.primaryContainer,
        tonalElevation = PlaceTimeCardDimen.CARD_ELEVATION
    ) {
        Row(
            modifier = Modifier.padding(all = MemoripPadding.PaddingMedium),
            horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MemoripImage(
                imageUrl = place.thumbnailImage.url,
                contentDescription = place.name,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(ratio = 1f)
                    .clip(shape = RoundedCornerShape(size = PlaceTimeCardDimen.IMAGE_CORNER_RADIUS)),
                contentScale = ContentScale.Crop
            )

            Column(verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall)) {
                Text(
                    text = place.name,
                    color = MemoripTheme.colors.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MemoripTheme.typography.headline2
                )
//                TagChipRow(tags = place.categories.toImmutableList())
                PlaceLocationText(address = place.address, maxLines = 2)
//                PlaceTimeText(
//                    startDateTime = place.startDateTime,
//                    endDateTime = place.endDateTime
//                )
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
        color = MemoripTheme.colors.primaryContainer,
        tonalElevation = PlaceTimeCardDimen.CARD_ELEVATION
    ) {
        Column {
            MemoripImage(
                imageUrl = place.thumbnailImage.url,
                contentDescription = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f)
                    .clip(shape = RoundedCornerShape(size = PlaceTimeCardDimen.IMAGE_CORNER_RADIUS)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = MemoripPadding.PaddingMedium)
                    .padding(bottom = MemoripPadding.PaddingSmall),
                verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall)
            ) {
                Text(
                    text = place.name,
                    color = MemoripTheme.colors.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MemoripTheme.typography.headline2,
                )
                TagChipRow(tags = place.categories.toImmutableList())
                Row(horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall)) {
                    PlaceLocationText(address = place.address)
                }
            }
        }
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