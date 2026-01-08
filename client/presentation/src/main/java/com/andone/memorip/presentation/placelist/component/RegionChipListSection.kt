package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.StaticChip
import com.andone.memorip.presentation.component.StaticChipColors
import com.andone.memorip.presentation.placelist.model.RegionChipModel
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun RegionChipListSection(
    modifier: Modifier = Modifier,
    regionList: List<RegionChipModel> = emptyList(),
    onChipClick: (id: String) -> Unit = {}
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall),
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
    ) {
        regionList.forEach { region ->
            StaticChip(
                chipName = region.name,
                modifier = Modifier.clickable(onClick = { onChipClick(region.id) }),
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
            regionList = listOf(
                RegionChipModel(id = "1", name = "서울", isSelected = true),
                RegionChipModel(id = "2", name = "경기"),
                RegionChipModel(id = "3", name = "인천"),
                RegionChipModel(id = "4", name = "강원"),
                RegionChipModel(id = "5", name = "충청북도"),
                RegionChipModel(id = "6", name = "충청남도"),
                RegionChipModel(id = "7", name = "전라북도"),
                RegionChipModel(id = "8", name = "전라남도", isSelected = true)
            ),
            onChipClick = {}
        )
    }
}