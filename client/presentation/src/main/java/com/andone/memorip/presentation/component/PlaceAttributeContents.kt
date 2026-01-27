package com.andone.memorip.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.theme.MemoripIconSize
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun TagChipRow(
    tags: ImmutableList<TagUiModel>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall)
    ) {
        tags.forEach { tag -> StaticTagChip(tag = tag) }
    }
}

@Composable
fun PlaceLocationText(
    address: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    iconTint: Color? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_location_on),
            contentDescription = stringResource(R.string.plan_location_content_description),
            modifier = Modifier.size(MemoripIconSize.IconSizeXSmall),
            tint = iconTint ?: Color.Unspecified,
        )
        Text(
            text = address,
            color = MemoripTheme.colors.onSurface,
            style = MemoripTheme.typography.labelRegular10,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
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