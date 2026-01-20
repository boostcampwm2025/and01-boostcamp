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
import com.andone.memorip.presentation.screen.plan.component.CenterDropdownTopAppBar
import com.andone.memorip.presentation.screen.plan.component.TimeTable
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.screen.plan.model.TimeBlock
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

@Composable
fun PlanScreen(modifier: Modifier = Modifier) {
    PlanScreenContents(modifier = modifier)
}

@Composable
fun PlanScreenContents(modifier: Modifier = Modifier) {
    var previewState by remember {
        mutableStateOf(PlanUiState(blocks = DummyData.timeBlocks))
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterDropdownTopAppBar(
                title = "",
                menuItems = emptyList(),
                onMenuItemClick = {},
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(paddingValues = innerPadding)) {
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