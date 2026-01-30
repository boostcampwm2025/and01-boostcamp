package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.millisToLocalDate
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeCalendar(
    initialStartDate: LocalDate?,
    initialEndDate: LocalDate?,
    onConfirm: (LocalDate, LocalDate) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberDateRangePickerState(
        initialSelectedStartDateMillis = initialStartDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()
            ?.toEpochMilli(),
        initialSelectedEndDateMillis = initialEndDate?.atStartOfDay(ZoneOffset.UTC)?.toInstant()
            ?.toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = state.selectedStartDateMillis != null && state.selectedEndDateMillis != null,
                onClick = {
                    val start = millisToLocalDate(state.selectedStartDateMillis!!)
                    val end = millisToLocalDate(state.selectedEndDateMillis!!)
                    onConfirm(start, end)
                }
            ) {
                Text(
                    text = stringResource(R.string.plan_confirm),
                    color = MemoripTheme.colors.primary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.plan_cancel),
                    color = MemoripTheme.colors.onSurface
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MemoripTheme.colors.background
        )
    ) {
        DateRangePicker(
            state = state,
            title = null,
            headline = null,
            colors = DatePickerDefaults.colors(
                containerColor = MemoripTheme.colors.background,
                selectedDayContainerColor = MemoripTheme.colors.primary,
                selectedDayContentColor = MemoripTheme.colors.white,
                todayContentColor = MemoripTheme.colors.primary,
                todayDateBorderColor = MemoripTheme.colors.primary,
                dayInSelectionRangeContainerColor = MemoripTheme.colors.primaryContainer,
                dayInSelectionRangeContentColor = MemoripTheme.colors.onSurface
            )
        )
    }
}

@Preview
@Composable
private fun DateRangeCalendarPreview() {
    MemoripTheme {
        DateRangeCalendar(
            initialStartDate = null,
            initialEndDate = null,
            onConfirm = { _, _ -> },
            onDismiss = { }
        )
    }
}