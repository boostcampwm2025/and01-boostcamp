package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.StaticChip
import com.andone.memorip.presentation.placelist.model.RegionChipModel
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R

@Composable
fun BottomSheetHeader(
    onChipClick: (id : String) -> Unit,
    modifier: Modifier = Modifier,
    selectedRegions: List<Map<Int, RegionChipModel>> = emptyList(),
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(state = rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
    ) {
        if (selectedRegions.isEmpty()){
            StaticChip(chipName = stringResource(R.string.place_list_region_default))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetHeaderPreview(){
    MemoripTheme {
        BottomSheetHeader(onChipClick = {})
    }
}