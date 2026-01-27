package com.andone.memorip.presentation.screen.plan

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.PlanBlockUiModel
import com.andone.memorip.presentation.model.toTimeBlock
import com.andone.memorip.presentation.screen.plan.PlanViewModelConstants.DAYS_LIMIT
import com.andone.memorip.presentation.screen.plan.model.DateUiModel
import com.andone.memorip.presentation.screen.plan.model.PlanAction
import com.andone.memorip.presentation.screen.plan.model.PlanEvent
import com.andone.memorip.presentation.screen.plan.model.PlanEvent.ShowDeleteDayDialog
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

private object PlanViewModelConstants {
    const val DAYS_LIMIT = 30
}

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val snackBarManager: SnackBarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        value = PlanUiState(
            places = DummyData.places.toImmutableList(),
//            blocks = DummyData.places.mapNotNull { it.toTimeBlock(dayStart = DummyData.dummyDate.startDay!!.atStartOfDay()) },
            blocks = emptyList(),
//            blockUiModels = DummyData.places.associateBy { it.id },
//            date = DummyData.dummyDate,
        )
    )
    val uiState = _uiState.asStateFlow()
    private val _event = Channel<PlanEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    private val originPlaces = uiState.value.places

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
                val date = uiState.value.date
                val (newStart, newEnd) = date.deleteDay(dayIndex = action.day)

                val newCurrent = adjustCurrentDay(
                    date = date,
                    dayIndex = action.day,
                    newStart = newStart,
                    newEnd = newEnd
                )

                adjustBlockUiModelsAfterDayRemoved(
                    blockUiModels = uiState.value.blockUiModels,
                    removedDayIndex = action.day,
                    date = uiState.value.date,
                    onUpdateState = { adjustedUiModels, value ->
                        val newBlocks = newStart?.let {
                            adjustedUiModels.values
                                .filterIsInstance<Place>()
                                .mapNotNull { place ->
                                    place.startDateTime?.let {
                                        place.toTimeBlock(dayStart = uiState.value.date.startDay!!.atStartOfDay())
                                    }
                                }
                        } ?: emptyList()

                        _uiState.update { state ->
                            state.copy(
                                date = date.copy(
                                    startDay = newStart,
                                    endDay = newEnd,
                                    currentDay = newCurrent,
                                    longClickedDay = null
                                ),
                                blockUiModels = adjustedUiModels,
                                blocks = newBlocks,
                                places = (uiState.value.places + value).toImmutableList()
                            )
                        }
                    }
                )
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
                _event.trySend(element = ShowDeleteDayDialog(day = _uiState.value.date.longClickedDay))
            }

            PlanAction.RemoveCancel -> {
                _uiState.update { it.copy(date = it.date.copy(longClickedDay = null)) }
            }

            is PlanAction.DateSelected -> {
                val dayDiff = ChronoUnit.DAYS.between(
                    action.start,
                    action.end
                ).days.toInt(DurationUnit.DAYS)

                if (dayDiff > DAYS_LIMIT) {
                    snackBarManager.show(SnackBarEvent.PLAN_DAYS_VALIDATION_ERROR)
                    return
                }

                _uiState.update {
                    it.copy(
                        date = it.date.copy(
                            startDay = action.start,
                            endDay = action.end,
                            currentDay = action.start
                        )
                    )
                }
            }

            is PlanAction.DayScrolled -> {
                _uiState.update {
                    it.copy(
                        date = it.date.copy(
                            currentDay = it.date.currentDayFromSelectedDay(
                                action.day
                            )
                        )
                    )
                }
            }

            is PlanAction.ItemDragEnd -> {
                val date = _uiState.value.date
                val baseDay = date.startDay ?: return

                val totalMinute = action.startMinute

                val dayOffset = totalMinute / MINUTES_PER_DAY
                val minuteInDay = totalMinute % MINUTES_PER_DAY

                val targetDay = baseDay.plusDays(dayOffset.toLong())

                val startDateTime = targetDay.atStartOfDay()
                    .plusMinutes(minuteInDay.toLong())

                val endDateTime = startDateTime.plusMinutes(action.item.durationMinutes)

                val isDuplicated = uiState.value.blocks.any {
                    val rangeBaseDay = baseDay.plusDays((it.day - 1).toLong())
                    val startRange =
                        rangeBaseDay.atStartOfDay().plusMinutes(it.startMinute.toLong())
                    val endRange =
                        rangeBaseDay.atStartOfDay().plusMinutes((it.endMinute - 1).toLong())
                    startDateTime in startRange..endRange
                }
                if (isDuplicated) {
                    return
                }

                val newPlace = action.item.copy(
                    startDateTime = startDateTime,
                    endDateTime = endDateTime
                )
                _uiState.update {
                    it.copy(
                        blockUiModels = it.blockUiModels + (action.item.id to newPlace),
                        blocks = uiState.value.blocks + newPlace.toTimeBlock(dayStart = uiState.value.date.startDay!!.atStartOfDay())!!,
                        places = (it.places - action.item).toImmutableList()
                    )
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

    private fun adjustBlockUiModelsAfterDayRemoved(
        blockUiModels: Map<String, PlanBlockUiModel>,
        removedDayIndex: Int,
        date: DateUiModel,
        onUpdateState: (Map<String, PlanBlockUiModel>, List<Place>) -> Unit
    ) {

        val removedDate = date.currentDayFromSelectedDay(removedDayIndex)

        val removedIds = blockUiModels
            .mapNotNull { (id, uiModel) ->
                if (uiModel is Place && removedDate != null) {
                    val date = uiModel.startDateTime?.toLocalDate()
                    if (date == removedDate) id else null
                } else null
            }
            .toSet()

        val removedPlaces = originPlaces.filter { it.id in removedIds }

        val blockUiModel = blockUiModels.mapNotNull { (id, uiModel) ->
            if (uiModel is Place && removedDate != null) {
                val place = uiModel

                val startDate = place.startDateTime?.toLocalDate()
                val endDateTime = place.endDateTime

                if (id in removedIds) {
                    return@mapNotNull id to place.copy(
                        startDateTime = null,
                        endDateTime = null
                    )
                }

                if (startDate == null || endDateTime == null) {
                    return@mapNotNull id to place
                }

                if (startDate.isAfter(removedDate)) {
                    return@mapNotNull id to place.copy(
                        startDateTime = place.startDateTime.minusDays(1),
                        endDateTime = place.endDateTime.minusDays(1)
                    )
                }

                return@mapNotNull id to place
            }

            id to uiModel
        }.toMap()

        onUpdateState(blockUiModel, removedPlaces)
    }
}
