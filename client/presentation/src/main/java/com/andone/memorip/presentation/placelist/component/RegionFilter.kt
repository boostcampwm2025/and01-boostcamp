package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.StaticChip
import com.andone.memorip.presentation.component.StaticChipColors
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes

@Composable
fun RegionFilter(
    modifier: Modifier = Modifier,
    parentRegion: String? = null,
    childRegion: String? = null,
    onChipClick: () -> Unit = {}
) {
    val regionText = when {
        parentRegion.isNullOrBlank() -> { stringResource(R.string.place_list_region_default) }
        childRegion.isNullOrBlank() -> { parentRegion }
        else -> { stringResource(R.string.place_list_region_hierarchy, parentRegion, childRegion) }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = memoripShapes.roundedXSmall)
            .background(color = MemoripTheme.colors.primaryContainer)
            .padding(
                horizontal = MemoripSpace.SpaceSmall,
                vertical = MemoripSpace.SpaceXSmall
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
    ) {
        Text(
            text = stringResource(R.string.place_list_region),
            style = MemoripTheme.typography.labelExtBold
        )
        Text(
            text = regionText,
            style = MemoripTheme.typography.label1
        )
        Spacer(modifier = Modifier.weight(weight = 1f))
        StaticChip(
            chipName = stringResource(R.string.place_list_region_change),
            colors = StaticChipColors.Default.copy(
                backgroundColor = MemoripTheme.colors.primary,
                textColor = MemoripTheme.colors.black
            ),
            textStyle = MemoripTheme.typography.labelExtBold,
            onClick = onChipClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationFilterPreview() {
    MemoripTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 5.dp)
        ) {
            RegionFilter(
                parentRegion = "서울",
                childRegion = "강남"
            )
            RegionFilter(parentRegion = "서울")
            RegionFilter()
        }
    }
}