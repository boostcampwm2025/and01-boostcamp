package com.andone.memorip.presentation.screen.plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.screen.plan.component.CenterDropdownTopAppBar
import com.andone.memorip.presentation.screen.plan.component.DayChipRow
import com.andone.memorip.presentation.screen.plan.component.TimeTable
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.R

@Composable
fun PlanScreen(
    modifier: Modifier = Modifier,
    viewModel: PlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PlanScreenContents(
        state = uiState,
        modifier = modifier
    )
}

@Composable
fun PlanScreenContents(
    state: PlanUiState,
    modifier: Modifier = Modifier
) {
    var previewState by remember {
        mutableStateOf(value = PlanUiState(blocks = DummyData.timeBlocks))
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterDropdownTopAppBar(title = stringResource(R.string.plan_default_group),)
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(paddingValues = innerPadding)) {
            DayChipRow(
                totalDays = 2,
                selectedDay = 2,
                onDaySelected = {},
            )
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
        PlanScreenContents(state = PlanUiState(blocks = DummyData.timeBlocks),)
    }
}