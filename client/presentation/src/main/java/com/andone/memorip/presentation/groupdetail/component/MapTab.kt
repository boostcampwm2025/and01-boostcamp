package com.andone.memorip.presentation.groupdetail.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun MapTab(modifier: Modifier = Modifier) {
    Text("map tab")
}

@Preview(showBackground = true)
@Composable
private fun MapTabPreview() {
    MemoripTheme {
        MapTab()
    }
}