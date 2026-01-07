package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.StaticChip
import com.andone.memorip.presentation.component.StaticChipColors
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun LocationFilter(
    modifier: Modifier = Modifier,
    region1: String? = null,
    region2: String? = null,
    onChipClick: () -> Unit = {}
) {
    val regionText = when {
        region1.isNullOrBlank() -> { stringResource(R.string.place_list_region_default) }
        region2.isNullOrBlank() -> { region1 }
        else -> { stringResource(R.string.place_list_region_hierarchy, region1, region2) }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceXSmall)
    ) {
        Text(
            text = stringResource(R.string.place_list_region),
            style = MemoripTheme.typography.title2
        )
        Text(text = regionText)
        Spacer(modifier = Modifier.weight(weight = 1f))
        StaticChip(
            chipName = stringResource(R.string.place_list_region_change),
            colors = StaticChipColors.Default,
            textStyle = MemoripTheme.typography.labelExtBold,
            onClick = onChipClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationFilterPreview(){
    MemoripTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 5.dp)
        ) {
            LocationFilter(
                region1 = "서울",
                region2 = "강남"
            )
            LocationFilter(region1 = "서울",)
            LocationFilter()
        }
    }
}