package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.placelist.model.RegionChipModel
import com.andone.memorip.presentation.placelist.model.SelectedRegionState
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripShadow
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun RegionSelectBottomSheet(
    modifier: Modifier = Modifier,
    regionMap: Map<Int, List<RegionChipModel>> = emptyMap(),
    regionLevel: Int = 0,
    selectedRegionState: SelectedRegionState = SelectedRegionState(),
    onConfirmClick: () -> Unit = {},
    onRegionChipClick: (id : String) -> Unit = {},
    onRegionTextClick: (id : String) -> Unit = {}
) {
    val currentRegionList = regionMap[regionLevel]

    Column(
        modifier = modifier.padding(horizontal = MemoripPadding.PaddingMedium),
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)
    ) {
        Text(
            text = stringResource(R.string.place_list_bottom_sheet_title),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MemoripTheme.typography.title1
        )

        RegionPathRow(
            selectedRegionState = selectedRegionState,
            onRegionTextClick = onRegionTextClick
        )

        HorizontalDivider()

        Button(
            onClick = onConfirmClick,
            modifier = Modifier.fillMaxWidth(),
            shape = memoripShapes.roundedXSmall,
        ) {
            Text(text = stringResource(R.string.place_list_bottom_sheet_confirm))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegionSelectBottomSheetPreview(){
    MemoripTheme {
        RegionSelectBottomSheet(onConfirmClick = {})
    }
}