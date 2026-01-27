package com.andone.memorip.presentation.screen.placelist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.ClickableChip
import com.andone.memorip.presentation.component.ChipColors
import com.andone.memorip.presentation.screen.placelist.model.RegionUiModel
import com.andone.memorip.presentation.screen.placelist.model.SelectedRegionState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
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
        modifier = modifier.padding(horizontal = MemoripPadding.AppHorizontalPadding),
        verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)
    ) {
        Text(
            text = stringResource(R.string.place_list_bottom_sheet_title),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MemoripTheme.typography.headlineBold20
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
            colors = ButtonDefaults.buttonColors()
                .copy(
                    containerColor = MemoripTheme.colors.primaryContainer,
                    contentColor = MemoripTheme.colors.onSurface
                )
        ) {
            Text(
                text = stringResource(R.string.place_list_bottom_sheet_confirm),
                style = MemoripTheme.typography.labelBold16
            )
        }
    }
}

@Composable
private fun RegionChipListSection(
    modifier: Modifier = Modifier,
    regionList: List<RegionUiModel> = emptyList(),
    onChipClick: (regionUiModel: RegionUiModel) -> Unit = {}
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceSmall),
    ) {
        regionList.forEach { region ->
            ClickableChip(
                chipName = region.name,
                colors =
                    if (region.isSelected) {
                        ChipColors.Selected
                    } else {
                        ChipColors.Default
                    },
                onClick = { onChipClick(region) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegionSelectBottomSheetPreview() {
    MemoripTheme {
        RegionSelectBottomSheet(
            currentRegionList = DummyData.regions.toList(),
            selectedRegionState = DummyData.regionState
        )
    }
}