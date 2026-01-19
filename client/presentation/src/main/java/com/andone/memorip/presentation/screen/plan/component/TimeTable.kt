package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.screen.plan.model.TableItem
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun TimeTable(
    modifier: Modifier = Modifier,
    tableItems: List<TableItem> = emptyList()
) {

}

@Preview(showBackground = true)
@Composable
private fun TimeTablePreview() {
    MemoripTheme {
        TimeTable()
    }
}