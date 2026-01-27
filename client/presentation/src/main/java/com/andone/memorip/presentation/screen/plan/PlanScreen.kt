package com.andone.memorip.presentation.screen.plan

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripButton
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.plan.component.DateContextBar
import com.andone.memorip.presentation.screen.plan.component.DateNotSelectedContent
import com.andone.memorip.presentation.screen.plan.component.DateRangeCalendar
import com.andone.memorip.presentation.screen.plan.component.DayChipRow
import com.andone.memorip.presentation.screen.plan.component.PlaceTimeCard
import com.andone.memorip.presentation.screen.plan.component.PlanTopAppBar
import com.andone.memorip.presentation.screen.plan.component.TimeBlockItem
import com.andone.memorip.presentation.screen.plan.component.TimeTable
import com.andone.memorip.presentation.screen.plan.model.PlanAction
import com.andone.memorip.presentation.screen.plan.model.PlanEvent
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import kotlinx.collections.immutable.toImmutableList

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
    var showCalendar by rememberSaveable { mutableStateOf(false) }

    if (showCalendar) {
        DateRangeCalendar(
            initialStartDate = state.date.startDay,
            initialEndDate = state.date.endDay,
            onConfirm = { start, end ->
                showCalendar = false
                onAction(PlanAction.DateSelected(start, end))
            },
            onDismiss = { showCalendar = false }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            PlanTopAppBar(
                title = stringResource(R.string.plan_default_group),
                isDeleteMode = state.date.longClickedDay != null,
                onDeleteClick = { onAction(PlanAction.RemoveDayClick) },
                onDismissClick = { onAction(PlanAction.RemoveCancel) }
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->

        if (state.date.startDay == null) {
            DateNotSelectedContent(onSelectDateClick = { showCalendar = true })
        } else {
            Column(modifier = Modifier.padding(paddingValues = innerPadding)) {
                DateSection(
                    state = state,
                    onAction = onAction,
                    showCalendar = { showCalendar = true }
                )

                TimeTable(
                    totalMinutes = state.date.totalMinutes,
                    currentDay = state.date.selectedDay,
                    places = state.places,
                    onBlockAdd = { place, start -> onAction(PlanAction.ItemDragEnd(place, start)) },
                    onDayScrolled = { day ->
                        onAction(PlanAction.DayScrolled(day))
                    }
                ) { engine, scrollState ->
                    state.blocks.forEach { block ->
                        TimeBlockItem(
                            block = block,
                            engine = engine,
                            scrollState = scrollState,
                            onMoved = { id, newStartMinute ->
                                onAction(PlanAction.BlockMoved(id, newStartMinute))
                            }
                        ) {
                            when (val uiModel = state.blockUiModels[block.id]) {
                                is Place -> {
                                    PlaceTimeCard(
                                        place = uiModel,
                                        onClick = {}
                                    )
                                }

                                null -> Unit
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateSection(
    state: PlanUiState,
    onAction: (PlanAction) -> Unit,
    showCalendar: () -> Unit,
) {
    DateContextBar(
        currentDate = state.date.currentDay,
        startDate = state.date.startDay,
        endDate = state.date.endDay,
        onClick = showCalendar
    )
    DayChipRow(
        totalDays = state.date.totalDays,
        selectedDay = state.date.selectedDay,
        onDaySelected = { onAction(PlanAction.SelectDay(day = it)) },
        onLongClick = { onAction(PlanAction.LongClick(day = it)) },
        onAddDayClick = { onAction(PlanAction.AddDay) },
        longClickedDay = state.date.longClickedDay
    )
}

@Preview(showBackground = true)
@Composable
private fun PlanScreenContentsPreview() {
    MemoripTheme {
        PlanScreenContents(
            state = PlanUiState(
                places = DummyData.places.toImmutableList(),
                blocks = DummyData.timeBlocks
            ),
            onAction = {},
        )
    }
}