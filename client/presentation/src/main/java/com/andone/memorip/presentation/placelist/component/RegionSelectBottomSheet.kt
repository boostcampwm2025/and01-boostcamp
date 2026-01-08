package com.andone.memorip.presentation.placelist.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun RegionSelectBottomSheet(
    parentRegions: List<String>,
    modifier: Modifier = Modifier,
    childRegions: List<String> = emptyList(),
    selectedRegions: List<List<String>> = emptyList(),
    onConfirmClick: () -> Unit = {}
) {
    Column(modifier = modifier) {

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