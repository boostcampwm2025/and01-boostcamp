package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.placelist.model.RegionChipModel
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.placelist.model.SelectedRegionState

@Composable
fun BottomSheetHeader(
    onChipClick: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    selectedRegionState: SelectedRegionState = SelectedRegionState(),
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selectedRegionState.parents.isEmpty() && selectedRegionState.children.isEmpty()) {
            Text(text = stringResource(R.string.place_list_region_default))
        } else {
            selectedRegionState.parents.forEachIndexed { index, region ->
                Text(
                    text = region.name,
                    style = MemoripTheme.typography.label1.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable {
                        onChipClick(region.id)
                    }
                )

                Text(
                    text = stringResource(R.string.place_list_region_divider),
                    style = MemoripTheme.typography.label1
                )
            }

            val childrenText = selectedRegionState.children
                .joinToString(", ") { it.name }

            Text(
                text = childrenText,
                style = MemoripTheme.typography.label1.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable {
                    selectedRegionState.children.firstOrNull()?.let {
                        onChipClick(it.id)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetHeaderPreview() {
    MemoripTheme {
        BottomSheetHeader(
            onChipClick = {},
            selectedRegionState = SelectedRegionState(
                parents = listOf(
                    RegionChipModel(id = "seoul", name = "서울", level = 0),
                    RegionChipModel(id = "gangnam", name = "강남구", level = 1)
                ),
                children = listOf(
                    RegionChipModel(id = "yeoksam", name = "역삼동", level = 2),
                    RegionChipModel(id = "samseong", name = "삼성동", level = 2)
                )
            )
        )
    }
}