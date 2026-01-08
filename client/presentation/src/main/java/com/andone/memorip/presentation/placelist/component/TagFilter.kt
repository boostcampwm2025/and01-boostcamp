package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.andone.memorip.presentation.theme.MemoripShadow

@Composable
fun TagFilter(
    tags: ImmutableList<Category>,
    modifier: Modifier = Modifier,
    onAddTagClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(state = rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
    ) {
        tags.forEach { tag -> TagChip(tag = tag) }
        StaticChip(
            chipName = stringResource(R.string.place_list_add_tag),
            modifier = Modifier.clickable(onClick = onAddTagClick),
            colors = StaticChipColors.Default.copy(
                backgroundColor = MemoripTheme.colors.primary,
                textColor = MemoripTheme.colors.black
            ),
            textStyle = MemoripTheme.typography.labelExtBold,
            elevation = MemoripShadow.Medium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TagFilterPreview() {
    MemoripTheme {
        Box(modifier = Modifier.padding(vertical = 16.dp)) {
            TagFilter(tags = DummyData.categories.toImmutableList())
        }
    }
}