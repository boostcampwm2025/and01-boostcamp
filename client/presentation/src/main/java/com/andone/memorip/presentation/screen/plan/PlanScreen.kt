package com.andone.memorip.presentation.screen.plan

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.andone.memorip.presentation.screen.plan.component.DateNotSelectedContent
import com.andone.memorip.presentation.screen.plan.component.DateRangeCalendar
import com.andone.memorip.presentation.screen.plan.component.DateSelectedContent
import com.andone.memorip.presentation.screen.plan.component.PlanTopAppBar
import com.andone.memorip.presentation.screen.plan.component.SelectGroupDialog
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
    var showGroupChoice by remember { mutableStateOf(false) }
    var showCalendar by rememberSaveable { mutableStateOf(false) }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            is PlanEvent.ShowDeleteDayDialog -> {
                deleteTargetDay = event.day
            }

            PlanEvent.ShowGroupChoiceDialog -> {
                showGroupChoice = true
            }

            PlanEvent.ShowCalendarDialog -> {
                showCalendar = true
            }
        }
    }

    LaunchedEffect(uiState) {
        Log.d("DEBUG TEST", "uiState blocks : ${uiState.blocks}")
        Log.d("DEBUG TEST", "uiState groups : ${uiState.groups}")
        Log.d("DEBUG TEST", "uiState places : ${uiState.places}")
        Log.d("DEBUG TEST", "uiState date : ${uiState.date}")
        Log.d("DEBUG TEST", "uiState selected group : ${uiState.selectedGroup}")
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.savePlan() }
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

    if (showGroupChoice) {
        SelectGroupDialog(
            groups = uiState.groups,
            onDismissRequest = { showGroupChoice = false },
            onConfirmClick = { group ->
                viewModel.onAction(
                    action = PlanAction.GroupChoiceConfirmClick(selectedGroup = group)
                )
            },
            onCancelClick = { showGroupChoice = false }
        )
    }

    if (showCalendar) {
        DateRangeCalendar(
            initialStartDate = uiState.date.startDay,
            initialEndDate = uiState.date.endDay,
            onConfirm = { start, end ->
                showCalendar = false
                viewModel.onAction(PlanAction.DateSelected(start, end))
            },
            onDismiss = { showCalendar = false }
        )
    }

    PlanScreenContents(
        state = uiState,
        showGroupChoice = showGroupChoice,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}

@Composable
fun PlanScreenContents(
    state: PlanUiState,
    showGroupChoice: Boolean,
    onAction: (PlanAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            PlanTopAppBar(
                title = state.selectedGroup?.title,
                groups = state.groups,
                expanded = showGroupChoice,
                isDeleteMode = state.date.longClickedDay != null,
                onTitleClick = { onAction(PlanAction.GroupChoiceClick) },
                onDeleteClick = { onAction(PlanAction.RemoveDayClick) },
                onDismissClick = { onAction(PlanAction.RemoveCancel) }
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->

        if (state.date.startDay == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding),
                contentAlignment = Alignment.Center
            ) {
                DateNotSelectedContent(onSelectDateClick = { onAction(PlanAction.ShowCalendarClick) })
            }
        } else {
            DateSelectedContent(
                state = state,
                onAction = onAction,
                modifier = Modifier.padding(paddingValues = innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlanScreenContentsPreview() {
    MemoripTheme {
        PlanScreenContents(
            state = PlanUiState(
                selectedGroup = DummyData.groupListItems.first(),
                groups = DummyData.groupListItems.toImmutableList(),
                places = DummyData.places.toImmutableList(),
                blocks = DummyData.timeBlocks
            ),
            showGroupChoice = false,
            onAction = {},
        )
    }
}