package com.andone.memorip.presentation.screen.plan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.andone.memorip.presentation.screen.plan.component.PlanTopAppBar
import com.andone.memorip.presentation.screen.plan.component.DayChipRow
import com.andone.memorip.presentation.screen.plan.component.TimeTable
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.plan.component.DateContextBar
import com.andone.memorip.presentation.screen.plan.model.PlanAction
import com.andone.memorip.presentation.screen.plan.model.PlanEvent
import com.andone.memorip.presentation.util.collectWithLifecycle
import java.time.LocalDate

@Composable
fun PlanScreen(
    modifier: Modifier = Modifier,
    viewModel: PlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var deleteTargetDay by remember { mutableStateOf<Int?>(value = null) }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            PlanEvent.ShowSnackBar -> {

            }

            is PlanEvent.ShowDeleteDayDialog -> {
                deleteTargetDay = event.day
            }
        }
    }

    deleteTargetDay?.let { day ->
        AlertDialog(
            onDismissRequest = {
                deleteTargetDay = null
                viewModel.onAction(action = PlanAction.RemoveCancel)
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onAction(action = PlanAction.RemoveDay(day))
                    deleteTargetDay = null
                }) {
                    Text(stringResource(R.string.plan_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    deleteTargetDay = null
                    viewModel.onAction(action = PlanAction.RemoveCancel)
                }) {
                    Text(stringResource(R.string.plan_cancel))
                }
            },
            title = { Text(stringResource(R.string.plan_day_delete)) },
            text = { Text(stringResource(R.string.plan_day_deleted_format, day)) }
        )
    }


    PlanScreenContents(
        state = uiState,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun PlanScreenContents(
    state: PlanUiState,
    onAction: (PlanAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PlanTopAppBar(
                title = stringResource(R.string.plan_default_group),
                isDeleteMode = state.longClickedDay != null,
                onDeleteClick = { onAction(PlanAction.RemoveDayClick) },
                onDismissClick = {onAction(PlanAction.RemoveCancel)}
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(paddingValues = innerPadding)) {
            DateContextBar(
                currentDate = LocalDate.of(2025, 1, 20),
                startDate = LocalDate.of(2025, 1, 20),
                endDate = LocalDate.of(2025, 1, 22),
                onClick = {}
            )
            DayChipRow(
                totalDays = state.totalDays,
                selectedDay = state.selectedDay,
                onDaySelected = { onAction(PlanAction.SelectDay(day = it)) },
                onLongClick = { onAction(PlanAction.LongClick(day = it)) },
                onAddDayClick = { onAction(PlanAction.AddDay) },
                longClickedDay = state.longClickedDay
            )
            TimeTable(
                blocks = state.blocks,
                onBlockMoved = { id, newStartMinute ->
                    onAction(PlanAction.BlockMoved(id, newStartMinute))
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlanScreenContentsPreview() {
    MemoripTheme {
        PlanScreenContents(
            state = PlanUiState(blocks = DummyData.timeBlocks),
            onAction = {},
        )
    }
}