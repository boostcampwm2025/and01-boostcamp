package com.andone.memorip.presentation.screen.plan

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun PlanScreen() {
    PlanScreenContents()
}

@Composable
fun PlanScreenContents() {

}

@Preview(showBackground = true)
@Composable
private fun PlanScreenContentsPreview() {
    MemoripTheme {
        PlanScreenContents()
    }
}