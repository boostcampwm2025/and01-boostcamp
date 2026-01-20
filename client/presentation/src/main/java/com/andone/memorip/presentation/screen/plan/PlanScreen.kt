package com.andone.memorip.presentation.screen.plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.screen.plan.component.TimeTable
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.screen.plan.model.TimeBlock
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun PlanScreen(modifier: Modifier = Modifier) {
    PlanScreenContents()
}

@Composable
fun PlanScreenContents(modifier: Modifier = Modifier) {
    var previewState by remember {
        mutableStateOf(
            PlanUiState(
                blocks = listOf(
                    TimeBlock("0", 0, 60),
                    TimeBlock("1", 3 * 60, 60),
                    TimeBlock("2", 11 * 60 + 30, 90),
                    TimeBlock("3", 15 * 60, 45)
                )
            )
        )
    }

    Scaffold(modifier = modifier) { innerPadding ->
        Column(modifier = modifier.padding(paddingValues = innerPadding)) {
            TimeTable(
                uiState = previewState,
                onBlockMoved = { id, newStartMinute ->
                    previewState = previewState.copy(
                        blocks = previewState.blocks.map { block ->
                            if (block.id == id) {
                                block.copy(startMinute = newStartMinute)
                            } else block
                        }
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlanScreenContentsPreview() {
    MemoripTheme {
        PlanScreenContents()
    }
}