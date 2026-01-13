package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.StaticChip
import com.andone.memorip.presentation.component.StaticChipColors
import com.andone.memorip.presentation.placelist.model.RegionUiModel
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

@Composable
fun RegionChipListSection(
    modifier: Modifier = Modifier,
    regionList: List<RegionUiModel> = emptyList(),
    onChipClick: (regionUiModel: RegionUiModel) -> Unit = {}
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall),
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall)
    ) {
        regionList.forEach { region ->
            StaticChip(
                chipName = region.name,
                modifier = Modifier.clickable(onClick = { onChipClick(region) }),
                colors =
                    if (region.isSelected) {
                        StaticChipColors.Selected
                    } else {
                        StaticChipColors.Default
                    },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegionChipListSectionPreview() {
    MemoripTheme {
        RegionChipListSection(
            regionList = DummyData.regions.toList(),
            onChipClick = {}
        )
    }
}