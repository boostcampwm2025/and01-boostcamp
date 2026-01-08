package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.placelist.model.RegionChipModel
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun RegionSelectBottomSheet(
    parentRegions: List<RegionChipModel>,
    modifier: Modifier = Modifier,
    childRegions: List<RegionChipModel> = emptyList(),
    selectedRegions: List<Map<Int, RegionChipModel>> = emptyList(),
    onConfirmClick: () -> Unit = {},
    onRegionClick: (id : String) -> Unit = {},
    onSelectedRegionClick: (id : String) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)
    ) {

    }
}

@Preview(showBackground = true)
@Composable
private fun RegionSelectBottomSheetPreview(){
    MemoripTheme {
        RegionSelectBottomSheet(
            parentRegions = emptyList(),
            childRegions = emptyList(),
            onConfirmClick = {}
        )
    }
}