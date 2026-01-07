package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.StaticChip
import com.andone.memorip.presentation.component.StaticChipColors
import com.andone.memorip.presentation.component.TagChip
import com.andone.memorip.presentation.model.Category
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import com.andone.memorip.presentation.R

@Composable
fun TagFilter(
    tags: ImmutableList<Category>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(state = rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
    ) {
        tags.forEach { tag -> TagChip(tag = tag) }
        StaticChip(
            chipName = stringResource(R.string.place_list_add_tag),
            colors = StaticChipColors.Default,
            textStyle = MemoripTheme.typography.labelExtBold,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun TagFilterPreview() {
    MemoripTheme {
        TagFilter(tags = DummyData.categories.toImmutableList())
    }
}