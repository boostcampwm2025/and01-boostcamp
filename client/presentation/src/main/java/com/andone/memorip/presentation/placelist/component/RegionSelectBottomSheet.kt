package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.placelist.model.RegionUiModel
import com.andone.memorip.presentation.placelist.model.SelectedRegionState
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.memoripShapes
import com.andone.memorip.presentation.util.DummyData

@Composable
fun RegionSelectBottomSheet(
    modifier: Modifier = Modifier,
    currentRegionList: List<RegionUiModel> = emptyList(),
    selectedRegionState: SelectedRegionState = SelectedRegionState(),
    onConfirmClick: () -> Unit = {},
    onRegionChipClick: (regionUiModel: RegionUiModel) -> Unit = {},
) {
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
            isUnderline = true
        )

        HorizontalDivider()

        RegionChipListSection(
            regionList = currentRegionList,
            onChipClick = onRegionChipClick
        )

        HorizontalDivider()

        Button(
            onClick = onConfirmClick,
            modifier = Modifier.fillMaxWidth(),
            shape = memoripShapes.roundedXSmall,
        ) {
            Text(
                text = stringResource(R.string.place_list_bottom_sheet_confirm),
                style = MemoripTheme.typography.title1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegionSelectBottomSheetPreview(){
    MemoripTheme {
        RegionSelectBottomSheet(
            currentRegionList = DummyData.regions.toList(),
            selectedRegionState = DummyData.regionState
        )
    }
}