package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.plan.model.PlanAction
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import kotlinx.collections.immutable.toImmutableList

@Composable
fun DateSelectedContent(
    state: PlanUiState,
    onAction: (PlanAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        DateSection(
            state = state,
            onAction = onAction,
            showCalendar = { onAction(PlanAction.ShowCalendarClick) }
        )

        TimeTable(
            totalMinutes = state.date.totalMinutes,
            currentDay = state.date.selectedDay,
            places = state.places.filter{ it.startDateTime == null && it.endDateTime == null }.toImmutableList(),
            onBlockAdd = { place, start -> onAction(PlanAction.BottomBlockDragEnd(place, start)) },
            onDayScrolled = { day ->
                onAction(PlanAction.DayScrolled(day))
            }
        ) { engine, scrollState ->
            state.places.filter { it.startDateTime != null && it.endDateTime != null }
                .forEach { block ->
                    TimeBlockItem(
                        block = block,
                        startDate = state.date.startDay!!,
                        engine = engine,
                        scrollState = scrollState,
                        onSlide = { id -> onAction(PlanAction.BlockSlide(id)) },
                        onMoved = { id, newStartMinute ->
                            onAction(PlanAction.BlockMoved(id, newStartMinute))
                        }
                    ) {
                        when (block) {
                            is Place -> {
                                PlaceTimeCard(
                                    place = block,
                                    onClick = { onAction(PlanAction.BlockClick(block.id)) }
                                )
                            }

                            null -> Unit
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
        onDaySelected = { onAction(PlanAction.DayClick(day = it)) },
        onLongClick = { onAction(PlanAction.DayLongClick(day = it)) },
        onAddDayClick = { onAction(PlanAction.AddDayClick) },
        longClickedDay = state.date.longClickedDay
    )
}

@Preview
@Composable
private fun DateSelectedContentPreview() {
    MemoripTheme {
        DateSelectedContent(
            state = PlanUiState(places = DummyData.places.toImmutableList()),
            onAction = { }
        )
    }
}