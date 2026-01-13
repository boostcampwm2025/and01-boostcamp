package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.placelist.model.SelectedRegionState
import com.andone.memorip.presentation.util.DummyData

@Composable
fun RegionPathRow(
    modifier: Modifier = Modifier,
    selectedRegionState: SelectedRegionState = SelectedRegionState(),
    isUnderline: Boolean = false,
) {
    val decoration = if (isUnderline) TextDecoration.Underline else TextDecoration.None

    val regionTextStyle = MemoripTheme.typography.label1.copy(textDecoration = decoration)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selectedRegionState.parents.isEmpty() && selectedRegionState.child.isEmpty()) {
            Text(
                text = stringResource(R.string.place_list_region_default),
                style = MemoripTheme.typography.label1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            selectedRegionState.parents.forEachIndexed { index, region ->
                Text(
                    text = region.name,
                    style = regionTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (selectedRegionState.parents.lastIndex != index || selectedRegionState.child.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.place_list_region_divider),
                        style = MemoripTheme.typography.label1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            val childrenText = selectedRegionState.child
                .joinToString(separator = stringResource(R.string.place_list_comma_separator)) { it.name }

            Text(
                text = childrenText,
                style = regionTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomSheetHeaderPreview() {
    MemoripTheme {
        RegionPathRow(
            selectedRegionState = DummyData.regionState,
            isUnderline = true
        )
    }
}