package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.dialog.DefaultDialog
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.AM_PM_THRESHOLD
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.EMPTY_MINUTE_VALUE
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.EMPTY_VALUE
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.FILL_CHAR
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.MINUTE_LENGTH
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogDimen.ITEM_SPACE
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogDimen.SPINNER_ITEM_HEIGHT
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogDimen.SPINNER_MAX_HEIGHT
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.toTimeString
import kotlinx.collections.immutable.toImmutableList
import java.time.LocalDateTime

private object PlanEditDialogDimen {
    val SPINNER_MAX_HEIGHT = 170.dp
    val SPINNER_ITEM_HEIGHT = 50.dp
    val ITEM_SPACE = 10.dp
}

private object PlanEditDialogConstant {
    const val AM_PM_THRESHOLD = 12
    const val MINUTE_LENGTH = 2
    const val FILL_CHAR = '0'
    const val EMPTY_VALUE = -1
    const val EMPTY_MINUTE_VALUE = -10
}

enum class PlanTimeType {
    START, END
}

@Composable
fun PlanEditDialog(
    defaultStartTime: LocalDateTime,
    defaultEndTime: LocalDateTime,
    modifier: Modifier = Modifier
) {
    var startTime by remember { mutableStateOf(value = defaultStartTime) }
    var endTime by remember { mutableStateOf(value = defaultEndTime) }
    var selectedTime by remember { mutableStateOf(value = PlanTimeType.START) }

    DefaultDialog(
        title = stringResource(R.string.plan_edit_dialog_title),
        modifier = modifier,
        onConfirmClick = {},
        onCancelClick = {},
        onDismissRequest = {},
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier
                    .background(
                        color = if (selectedTime == PlanTimeType.START) MemoripTheme.colors.primaryContainer else MemoripTheme.colors.transparent,
                        shape = MemoripTheme.shapes.roundedMedium
                    )
                    .clip(shape = MemoripTheme.shapes.roundedMedium)
                    .clickable {
                        selectedTime = PlanTimeType.START
                    },
                text = startTime.toTimeString(),
                color = MemoripTheme.colors.onSurface,
                style = MemoripTheme.typography.bodyMedium16
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_forward),
                contentDescription = null,
                tint = MemoripTheme.colors.onSurface
            )
            Text(
                modifier = Modifier
                    .background(
                        color = if (selectedTime == PlanTimeType.END) MemoripTheme.colors.primaryContainer else MemoripTheme.colors.transparent,
                        shape = MemoripTheme.shapes.roundedMedium
                    )
                    .clip(shape = MemoripTheme.shapes.roundedMedium)
                    .clickable {
                        selectedTime = PlanTimeType.END
                    },
                text = endTime.toTimeString(),
                color = MemoripTheme.colors.onSurface,
                style = MemoripTheme.typography.bodyMedium16
            )
        }

        HorizontalDivider(thickness = MemoripLineWidth.Thin, color = MemoripTheme.colors.lightGray)

        TimeSpinner(
            time = if (selectedTime == PlanTimeType.START) startTime else endTime,
            onTimeChange = {
                if (selectedTime == PlanTimeType.START) startTime = it else endTime = it
            }
        )
    }
}

@Composable
private fun TimeSpinner(
    time: LocalDateTime,
    onTimeChange: (LocalDateTime) -> Unit
) {
    val amPm =
        listOf(EMPTY_VALUE.toString()) + stringArrayResource(R.array.plan_edit_dialog_am_pm).toList() + listOf(
            EMPTY_VALUE.toString()
        )
    val hours = (EMPTY_VALUE..11).plus(EMPTY_VALUE).toImmutableList()
    val minutes = (EMPTY_VALUE..5).plus(EMPTY_VALUE).map { it * 10 }.toImmutableList()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = MemoripPadding.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyColumn(
            modifier = Modifier
                .height(height = SPINNER_MAX_HEIGHT)
                .weight(3f),
            verticalArrangement = Arrangement.spacedBy(space = ITEM_SPACE),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(items = amPm) { idx, amPm ->
                val isSelected =
                    (idx == 1 && time.hour < AM_PM_THRESHOLD) || (idx == 2 && time.hour >= AM_PM_THRESHOLD)
                Box(
                    modifier = Modifier.height(height = SPINNER_ITEM_HEIGHT),
                    contentAlignment = Alignment.Center
                ) {
                    if (amPm != EMPTY_VALUE.toString()) {
                        TextButton(
                            onClick = {
                                val newTime =
                                    if (idx == 1) time.withHour(time.hour)
                                    else time.withHour(time.hour + AM_PM_THRESHOLD)
                                onTimeChange(newTime)
                            }
                        ) {
                            Text(
                                text = amPm,
                                color = if (isSelected) MemoripTheme.colors.onSurface else MemoripTheme.colors.lightGray,
                                style = if (isSelected) MemoripTheme.typography.bodyBold16 else MemoripTheme.typography.bodyMedium14
                            )
                        }
                    }
                }
            }
        }
        Text(
            text = stringResource(R.string.plan_edit_dialog_divider),
            color = MemoripTheme.colors.transparent,
            style = MemoripTheme.typography.bodyBold14
        )
        LazyColumn(
            modifier = Modifier
                .height(height = SPINNER_MAX_HEIGHT)
                .weight(2f),
            verticalArrangement = Arrangement.spacedBy(space = ITEM_SPACE),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = hours) { hour ->
                Box(
                    modifier = Modifier.height(height = SPINNER_ITEM_HEIGHT),
                    contentAlignment = Alignment.Center
                ) {
                    if (hour != EMPTY_VALUE) {
                        TextButton(
                            onClick = {
                                val newTime =
                                    if (time.hour > AM_PM_THRESHOLD) time.withHour(AM_PM_THRESHOLD + hour)
                                    else time.withHour(hour)
                                onTimeChange(newTime)
                            }
                        ) {
                            Text(
                                text = hour.toString(),
                                color = if (time.hour % AM_PM_THRESHOLD == hour) MemoripTheme.colors.onSurface else MemoripTheme.colors.lightGray,
                                style = if (time.hour % AM_PM_THRESHOLD == hour) MemoripTheme.typography.bodyBold16 else MemoripTheme.typography.bodyMedium14
                            )
                        }
                    }
                }
            }
        }
        Text(
            text = stringResource(R.string.plan_edit_dialog_divider),
            color = MemoripTheme.colors.onSurface,
            style = MemoripTheme.typography.bodyBold14
        )
        LazyColumn(
            modifier = Modifier
                .height(height = SPINNER_MAX_HEIGHT)
                .weight(2f),
            verticalArrangement = Arrangement.spacedBy(space = ITEM_SPACE),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = minutes) { minute ->
                Box(
                    modifier = Modifier.height(height = SPINNER_ITEM_HEIGHT),
                    contentAlignment = Alignment.Center
                ) {
                    if (minute != EMPTY_MINUTE_VALUE) {
                        TextButton(
                            onClick = {
                                val newTime = time.withMinute(minute)
                                onTimeChange(newTime)
                            }
                        ) {
                            Text(
                                text = minute.toString()
                                    .padStart(length = MINUTE_LENGTH, padChar = FILL_CHAR),
                                color = if (time.minute == minute) MemoripTheme.colors.onSurface else MemoripTheme.colors.lightGray,
                                style = if (time.minute == minute) MemoripTheme.typography.bodyBold16 else MemoripTheme.typography.bodyMedium14
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PlanEditDialogPreview() {
    MemoripTheme {
        val today = LocalDateTime.now()
        val time = today.withMinute(today.minute - today.minute % 5)
        PlanEditDialog(
            defaultStartTime = time,
            defaultEndTime = time
        )
    }
}