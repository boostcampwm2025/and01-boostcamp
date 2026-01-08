package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.model.Category
import com.andone.memorip.presentation.placelist.model.SelectedRegionState
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FilterSection(
    onChangeRegionClick: () -> Unit,
    onAddTagClick: () -> Unit,
    modifier: Modifier = Modifier,
    tags: ImmutableList<Category> = persistentListOf(),
    selectedRegionState: SelectedRegionState = SelectedRegionState(),
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall)
    ) {
        RegionFilter(
            modifier = Modifier.clickable(onClick = onChangeRegionClick),
            selectedRegionState = selectedRegionState,
        )
        TagFilter(
            tags = tags,
            onAddTagClick = onAddTagClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterSectionPreview() {
    MemoripTheme {
        FilterSection(
            tags = DummyData.categories.toImmutableList(),
            onChangeRegionClick = {},
            onAddTagClick = {},
        )
    }
}