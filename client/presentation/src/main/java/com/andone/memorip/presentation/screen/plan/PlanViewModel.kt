package com.andone.memorip.presentation.screen.plan

import androidx.lifecycle.ViewModel
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.presentation.model.toTimeBlock
import com.andone.memorip.presentation.screen.plan.model.DateUiModel
import com.andone.memorip.presentation.screen.plan.model.PlanAction
import com.andone.memorip.presentation.screen.plan.model.PlanEvent
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.util.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(value = PlanUiState(
        blocks = DummyData.places.map { it.toTimeBlock(dayStart = DummyData.dummyDate.startDay!!.atStartOfDay()) },
        blockUiModels = DummyData.places.associateBy { it.id },
        date = DummyData.dummyDate,
    ))
    val uiState = _uiState.asStateFlow()
    private val _event = Channel<PlanEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: PlanAction) {
        when (action) {
            is PlanAction.BlockMoved -> {
                moveBlock(action.id, action.newStartMinute)
            }

            PlanAction.AddDay -> {
                _uiState.update {
                    it.copy(date = it.date.copy(endDay = it.date.endDay?.plusDays(1)))
                }
            }

            is PlanAction.RemoveDay -> {
                _uiState.update { state ->
                    val date = state.date
                    val (newStart, newEnd) = date.deleteDay(dayIndex = action.day)

                    val newCurrent = adjustCurrentDay(
                        date = date,
                        dayIndex = action.day,
                        newStart = newStart,
                        newEnd = newEnd
                    )

                    state.copy(
                        date = date.copy(
                            startDay = newStart,
                            endDay = newEnd,
                            currentDay = newCurrent,
                            longClickedDay = null
                        ),
                    )
                }
            }


            is PlanAction.LongClick -> {
                _uiState.update { it.copy(date = it.date.copy(longClickedDay = action.day)) }
            }

            is PlanAction.SelectDay -> {
                _uiState.update {
                    it.copy(date = it.date.copy(currentDay = it.date.currentDayFromSelectedDay(selectedDay = action.day)))
                }
            }

            PlanAction.RemoveDayClick -> {
                _event.trySend(element = PlanEvent.ShowDeleteDayDialog(day = _uiState.value.date.longClickedDay))
            }

            PlanAction.RemoveCancel -> {
                _uiState.update { it.copy(date = it.date.copy(longClickedDay = null)) }
            }

            is PlanAction.DateSelected -> {
                _uiState.update {
                    it.copy(date = it.date.copy(startDay = action.start, endDay = action.end, currentDay = action.start))
                }
            }

            is PlanAction.DayScrolled -> {
                _uiState.update {
                    it.copy(date = it.date.copy(currentDay = it.date.currentDayFromSelectedDay(action.day)))
                }
            }
        }
    }

    private fun moveBlock(id: String, newStartMinute: Int) {
        _uiState.update { state ->
            val target = state.blocks.find { it.id == id } ?: return@update state

            if (!target.canMoveTo(
                    newStartMinute = newStartMinute,
                    blocks = state.blocks,
                    totalMinutes = state.date.totalMinutes
                )
            ) {
                return@update state
            }

            state.copy(
                blocks = state.blocks.map { block ->
                    if (block.id == id)
                        block.movedTo(newStartMinute, state.date.totalMinutes)
                    else block
                }
            )
        }
    }

    private fun adjustCurrentDay(
        date: DateUiModel,
        dayIndex: Int,
        newStart: LocalDate?,
        newEnd: LocalDate?
    ): LocalDate? {
        if (newStart == null || newEnd == null) return null

        val deletedDay = date.currentDayFromSelectedDay(dayIndex)
        val current = date.currentDay ?: return newStart

        return when {
            deletedDay != null && current.isEqual(deletedDay) -> {
                val next = deletedDay.plusDays(1)
                when {
                    next.isAfter(newEnd) -> newEnd
                    next.isBefore(newStart) -> newStart
                    else -> next
                }
            }
            current.isAfter(newEnd) -> newEnd
            current.isBefore(newStart) -> newStart
            else -> current
        }
    }

    private fun adjustblockUiModelsAfterDayRemoved(
        blocks: List<TimeBlock>,
        removedDayIndex: Int
    ): List<TimeBlock> {
        return blocks
            .filterNot { it.day == removedDayIndex }
            .map { block ->
                if (block.day > removedDayIndex) {
                    block.copy(day = block.day - 1)
                } else {
                    block
                }
            }
    }
}
