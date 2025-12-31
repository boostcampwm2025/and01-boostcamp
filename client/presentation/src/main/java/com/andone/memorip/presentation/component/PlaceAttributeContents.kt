package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.model.Category
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.formatTimeRange
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDateTime

@Composable
fun TagChipRow(
    tags: ImmutableList<Category>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
    ) {
        tags.forEach { tag -> TagChip(tag = tag) }
    }
}

@Composable
fun PlaceLocationText(
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
fun PlaceTimeText(
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
        modifier = modifier
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

