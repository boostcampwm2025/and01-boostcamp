package com.andone.memorip.presentation.screen.placelist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.placelist.component.RegionPathRow
import com.andone.memorip.presentation.placelist.model.SelectedRegionState
import com.andone.memorip.presentation.theme.MemoripShadow
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import com.andone.memorip.presentation.util.DummyData

@Composable
fun RegionFilter(
    modifier: Modifier = Modifier,
    selectedRegionState: SelectedRegionState = SelectedRegionState(),
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = memoripShapes.roundedXSmall,
        color = MemoripTheme.colors.primaryContainer,
        contentColor = MemoripTheme.colors.onSurface,
        shadowElevation = MemoripShadow.Medium
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = MemoripSpace.SpaceSmall,
                vertical = MemoripSpace.SpaceXSmall
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall)
        ) {
            Text(
                text = stringResource(R.string.place_list_region),
                style = MemoripTheme.typography.labelExtBold
            )
            RegionPathRow(selectedRegionState = selectedRegionState)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationFilterPreview() {
    MemoripTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RegionFilter(selectedRegionState = DummyData.regionState)
            RegionFilter()
        }
    }
}