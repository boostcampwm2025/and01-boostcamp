package com.andone.memorip.presentation.screen.plan

import androidx.lifecycle.ViewModel
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
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor() : ViewModel() {

    val _uiState = MutableStateFlow(value = PlanUiState(blocks = DummyData.timeBlocks))
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
                    val (newStart, newEnd) = state.date.deleteDay(dayIndex = action.day)
                    state.copy(
                        date = state.date.copy(
                            startDay = newStart,
                            endDay = newEnd,
                            longClickedDay = null
                        )
                    )
                }
            }

            is PlanAction.LongClick -> {
                _uiState.update { it.copy(date = it.date.copy(longClickedDay = action.day)) }
            }

            is PlanAction.SelectDay -> {
                _uiState.update {
                    it.copy(
                        date = it.date.copy(
                            currentDay = it.date.currentDayFromSelectedDay(
                                selectedDay = action.day
                            )
                        )
                    )
                }
            }

            PlanAction.RemoveDayClick -> {
                _event.trySend(element = PlanEvent.ShowDeleteDayDialog(day = _uiState.value.date.longClickedDay))
            }

            PlanAction.RemoveCancel -> {
                _uiState.update { it.copy(date = it.date.copy(longClickedDay = null)) }
            }

            is PlanAction.DateSelected -> {

            }
        }
    }

    private fun moveBlock(id: String, newStartMinute: Int) {
        _uiState.update {
            it.copy(
                blocks = it.blocks.map { block ->
                    if (block.id == id) block.copy(startMinute = newStartMinute)
                    else block
                }
            )
        }
    }
}
