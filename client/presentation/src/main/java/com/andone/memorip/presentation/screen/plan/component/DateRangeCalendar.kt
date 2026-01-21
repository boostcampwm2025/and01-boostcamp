package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.andone.memorip.presentation.util.millisToLocalDate
import java.time.LocalDate
import com.andone.memorip.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeCalendar(
    onConfirm: (LocalDate, LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val state = rememberDateRangePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = state.selectedStartDateMillis != null &&
                        state.selectedEndDateMillis != null,
                onClick = {
                    val start = millisToLocalDate(state.selectedStartDateMillis!!)
                    val end = millisToLocalDate(state.selectedEndDateMillis!!)
                    onConfirm(start, end)
                }
            ) {
                Text(text = stringResource(R.string.plan_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.plan_cancel))
            }
        },
        text = {
            DateRangePicker(state = state)
        }
    )
}
