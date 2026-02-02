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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.dialog.DefaultDialog
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.AM_IDX
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.AM_PM_THRESHOLD
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.DECO_ALPHA
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.EMPTY_MINUTE_VALUE
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.EMPTY_VALUE
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.FILL_CHAR
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.MINUTE_LENGTH
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.MINUTE_STEP
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogConstant.PM_IDX
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogDimen.ITEM_SPACE
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogDimen.SPINNER_ITEM_HEIGHT
import com.andone.memorip.presentation.screen.plan.component.PlanEditDialogDimen.SPINNER_MAX_HEIGHT
import com.andone.memorip.presentation.theme.MemoripLineWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.toPx
import com.andone.memorip.presentation.util.toTimeString
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
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
    const val AM_IDX = 1
    const val PM_IDX = 2
    const val MINUTE_STEP = 10
    const val DECO_ALPHA = 0.5f
}

enum class PlanTimeType {
    START, END
}

@Composable
fun PlanEditDialog(
    defaultStartTime: LocalDateTime,
    defaultEndTime: LocalDateTime,
    onConfirmClick: (LocalDateTime, LocalDateTime) -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var startTime by remember { mutableStateOf(value = defaultStartTime) }
    var endTime by remember { mutableStateOf(value = defaultEndTime) }
    var selectedTime by remember { mutableStateOf(value = PlanTimeType.START) }
    var flag by remember { mutableStateOf(value = false) }

    DefaultDialog(
        title = stringResource(R.string.plan_edit_dialog_title),
        modifier = modifier,
        onConfirmClick = { onConfirmClick(startTime, endTime) },
        onCancelClick = onCancelClick,
        onDismissRequest = onDismissRequest,
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
                        flag = !flag
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
                        flag = !flag
                    },
                text = endTime.toTimeString(),
                color = MemoripTheme.colors.onSurface,
                style = MemoripTheme.typography.bodyMedium16
            )
        }

        HorizontalDivider(thickness = MemoripLineWidth.Thin, color = MemoripTheme.colors.lightGray)

        Box(
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = SPINNER_ITEM_HEIGHT)
                    .background(
                        color = MemoripTheme.colors.lightGray.copy(alpha = DECO_ALPHA),
                        shape = MemoripTheme.shapes.roundedMedium
                    ),
            )

            TimeSpinner(
                flag = flag,
                time = if (selectedTime == PlanTimeType.START) startTime else endTime,
                onTimeChange = {
                    if (selectedTime == PlanTimeType.START) {
                        startTime = it
                        if (startTime.isAfter(endTime)) {
                            endTime = it
                        }
                    } else {
                        endTime = it
                        if (endTime.isBefore(startTime)) {
                            startTime = it
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun TimeSpinner(
    flag: Boolean,
    time: LocalDateTime,
    onTimeChange: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val lastestTime by rememberUpdatedState(time)
    val amPmList = stringArrayResource(R.array.plan_edit_dialog_am_pm).toList()
    val amPm =
        remember { listOf(EMPTY_VALUE.toString()) + amPmList + listOf(EMPTY_VALUE.toString()) }
    val hours = remember { listOf(EMPTY_VALUE) + (0..11).plus(EMPTY_VALUE).toImmutableList() }
    val minutes = remember { (EMPTY_VALUE..5).plus(EMPTY_VALUE).map { it * 10 }.toImmutableList() }
    val firstIndex = remember { if (time.hour > AM_PM_THRESHOLD) PM_IDX else AM_IDX }
    val amPmScrollState = rememberLazyListState(initialFirstVisibleItemIndex = firstIndex)
    val hourScrollState =
        rememberLazyListState(initialFirstVisibleItemIndex = hours.indexOf(time.hour % AM_PM_THRESHOLD))
    val minuteScrollState =
        rememberLazyListState(initialFirstVisibleItemIndex = minutes.indexOf(time.minute - time.minute % MINUTE_STEP))
    val density = LocalDensity.current
    val centerOffset = (SPINNER_MAX_HEIGHT - SPINNER_ITEM_HEIGHT) / 2
    val centerOffsetPx = centerOffset.toPx(density).toInt()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(flag) {
        val targetMinute = minutes.indexOf(time.minute - time.minute % MINUTE_STEP)
        minuteScrollState.scrollToItem(targetMinute, -centerOffsetPx)

        val targetHour = hours.indexOf(time.hour % AM_PM_THRESHOLD)
        hourScrollState.scrollToItem(targetHour, -centerOffsetPx)

        val targetAmPm = if (time.hour > AM_PM_THRESHOLD) PM_IDX else AM_IDX
        amPmScrollState.scrollToItem(targetAmPm, -centerOffsetPx)
    }

    LaunchedEffect(hourScrollState) {
        snapshotFlow { hourScrollState.isScrollInProgress }
            .filter { !it }
            .collect {
                val layoutInfo = hourScrollState.layoutInfo
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

                val closestItem = layoutInfo.visibleItemsInfo.filter { it.key != EMPTY_VALUE }
                    .minByOrNull { item ->
                        kotlin.math.abs(
                            (item.offset + item.size / 2) - viewportCenter
                        )
                    } ?: return@collect

                hourScrollState.animateScrollToItem(
                    index = closestItem.index,
                    scrollOffset = -centerOffsetPx
                )

                val selectedHour = hours[closestItem.index]
                if (selectedHour != EMPTY_VALUE) {
                    val newTime =
                        if (lastestTime.hour > AM_PM_THRESHOLD) lastestTime.withHour(AM_PM_THRESHOLD + selectedHour)
                        else lastestTime.withHour(selectedHour)

                    onTimeChange(newTime)
                }
            }
    }

    LaunchedEffect(minuteScrollState) {
        snapshotFlow { minuteScrollState.isScrollInProgress }
            .filter { !it }
            .collect {
                val layoutInfo = minuteScrollState.layoutInfo
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

                val closestItem =
                    layoutInfo.visibleItemsInfo.filter { it.key != EMPTY_MINUTE_VALUE }
                        .minByOrNull { item ->
                            kotlin.math.abs((item.offset + item.size / 2) - viewportCenter)
                        } ?: return@collect

                minuteScrollState.animateScrollToItem(
                    index = closestItem.index,
                    scrollOffset = -centerOffsetPx
                )

                val selectedMinute = minutes[closestItem.index]
                if (selectedMinute != EMPTY_MINUTE_VALUE) {
                    val newTime = lastestTime.withMinute(selectedMinute)

                    onTimeChange(newTime)
                }
            }
    }

    LaunchedEffect(amPmScrollState) {
        snapshotFlow { amPmScrollState.isScrollInProgress }
            .filter { !it }
            .collect {
                val layoutInfo = amPmScrollState.layoutInfo
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

                val closestItem =
                    layoutInfo.visibleItemsInfo.filter { it.key != EMPTY_VALUE.toString() }
                        .minByOrNull { item ->
                            kotlin.math.abs((item.offset + item.size / 2) - viewportCenter)
                        } ?: return@collect

                amPmScrollState.animateScrollToItem(
                    index = closestItem.index,
                    scrollOffset = -centerOffsetPx
                )

                val selectedAmPm = amPm[closestItem.index]
                if (selectedAmPm != EMPTY_VALUE.toString()) {
                    val newTime =
                        if (closestItem.index == AM_IDX) lastestTime.withHour(lastestTime.hour % AM_PM_THRESHOLD)
                        else if (closestItem.index == PM_IDX && lastestTime.hour < AM_PM_THRESHOLD) lastestTime.withHour(
                            lastestTime.hour + AM_PM_THRESHOLD
                        )
                        else lastestTime

                    onTimeChange(newTime)
                }
            }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = MemoripPadding.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyColumn(
            modifier = Modifier
                .height(height = SPINNER_MAX_HEIGHT)
                .weight(3f),
            verticalArrangement = Arrangement.spacedBy(space = ITEM_SPACE),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = amPmScrollState
        ) {
            itemsIndexed(items = amPm) { idx, amPm ->
                val isSelected =
                    (idx == AM_IDX && time.hour < AM_PM_THRESHOLD) || (idx == PM_IDX && time.hour >= AM_PM_THRESHOLD)
                Box(
                    modifier = Modifier.height(height = SPINNER_ITEM_HEIGHT),
                    contentAlignment = Alignment.Center
                ) {
                    if (amPm != EMPTY_VALUE.toString()) {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    amPmScrollState.animateScrollToItem(
                                        index = idx,
                                        scrollOffset = -centerOffsetPx
                                    )
                                }

                                val newTime =
                                    if (idx == AM_IDX) lastestTime.withHour(lastestTime.hour % AM_PM_THRESHOLD)
                                    else lastestTime.withHour(if (lastestTime.hour >= AM_PM_THRESHOLD) lastestTime.hour else lastestTime.hour + AM_PM_THRESHOLD)
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
            horizontalAlignment = Alignment.CenterHorizontally,
            state = hourScrollState
        ) {
            items(items = hours) { hour ->
                Box(
                    modifier = Modifier.height(height = SPINNER_ITEM_HEIGHT),
                    contentAlignment = Alignment.Center
                ) {
                    if (hour != EMPTY_VALUE) {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    val idx = hours.indexOf(hour)
                                    hourScrollState.animateScrollToItem(
                                        index = idx,
                                        scrollOffset = -centerOffsetPx
                                    )
                                }

                                val newTime =
                                    if (lastestTime.hour > AM_PM_THRESHOLD) lastestTime.withHour(AM_PM_THRESHOLD + hour)
                                    else lastestTime.withHour(hour)
                                onTimeChange(newTime)
                            }
                        ) {
                            Text(
                                text = hour.toString(),
                                color = if (lastestTime.hour % AM_PM_THRESHOLD == hour) MemoripTheme.colors.onSurface else MemoripTheme.colors.lightGray,
                                style = if (lastestTime.hour % AM_PM_THRESHOLD == hour) MemoripTheme.typography.bodyBold16 else MemoripTheme.typography.bodyMedium14
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
            horizontalAlignment = Alignment.CenterHorizontally,
            state = minuteScrollState
        ) {
            items(items = minutes) { minute ->
                Box(
                    modifier = Modifier.height(height = SPINNER_ITEM_HEIGHT),
                    contentAlignment = Alignment.Center
                ) {
                    if (minute != EMPTY_MINUTE_VALUE) {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    val idx = minutes.indexOf(minute)
                                    minuteScrollState.animateScrollToItem(
                                        index = idx,
                                        scrollOffset = -centerOffsetPx
                                    )
                                }
                                val newTime = lastestTime.withMinute(minute)
                                onTimeChange(newTime)
                            }
                        ) {
                            Text(
                                text = minute.toString()
                                    .padStart(length = MINUTE_LENGTH, padChar = FILL_CHAR),
                                color = if (lastestTime.minute == minute) MemoripTheme.colors.onSurface else MemoripTheme.colors.lightGray,
                                style = if (lastestTime.minute == minute) MemoripTheme.typography.bodyBold16 else MemoripTheme.typography.bodyMedium14
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
        val time = today.withMinute(today.minute - today.minute % 10)
        PlanEditDialog(
            defaultStartTime = time,
            defaultEndTime = time,
            onConfirmClick = { _, _ -> },
            onCancelClick = { },
            onDismissRequest = { }
        )
    }
}