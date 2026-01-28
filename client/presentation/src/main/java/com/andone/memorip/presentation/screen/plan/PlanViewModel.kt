package com.andone.memorip.presentation.screen.plan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.model.GroupListItem
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.domain.repository.GroupRepository
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.presentation.model.GroupUiModel
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.PlanBlockUiModel
import com.andone.memorip.presentation.model.toTimeBlock
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.plan.PlanViewModelConstants.DAYS_LIMIT
import com.andone.memorip.presentation.screen.plan.model.DateUiModel
import com.andone.memorip.presentation.screen.plan.model.GroupListUiModel
import com.andone.memorip.presentation.screen.plan.model.PlanAction
import com.andone.memorip.presentation.screen.plan.model.PlanEvent
import com.andone.memorip.presentation.screen.plan.model.PlanEvent.ShowDeleteDayDialog
import com.andone.memorip.presentation.screen.plan.model.PlanGroupUiModel
import com.andone.memorip.presentation.screen.plan.model.PlanPlaceUiModel
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    private val groupRepository: GroupRepository,
    private val placeRepository: PlaceRepository,
    private val snackBarManager: SnackBarManager
) : ViewModel() {

    private val myGroupsFlow = flow{
        var result = emptyList<GroupListItem>()
        groupRepository.getSimpleGroups()
            .onSuccess{ result = it }
            .onFailure{ snackBarManager.show(event = SnackBarEvent.NETWORK_ERROR) }
        emit(result)
    }
    private val selectedGroupFlow = MutableStateFlow<GroupListUiModel?>(value = null)
    private val groupFlow = combine(selectedGroupFlow, myGroupsFlow) { selectedGroup, myGroups ->
        PlanGroupUiModel(
            selectedGroup = selectedGroup,
            groups = myGroups.map { GroupListUiModel.from(it) }.toImmutableList()
        )
    }
    private val placesFlow = MutableStateFlow<List<Place>>(emptyList())
    private val blocksFlow = MutableStateFlow(value = emptyList<TimeBlock>())
    private val dateFlow = MutableStateFlow(value = DateUiModel())
    private val blockUiModelsFlow = MutableStateFlow(value = emptyMap<String, PlanBlockUiModel>())
    private val placeFlow = combine(
        placesFlow,
        blocksFlow,
        blockUiModelsFlow
    ) { places, blocks, blockUiModels ->
        PlanPlaceUiModel(
            places = places.toImmutableList(),
            blocks = blocks.toImmutableList(),
            blockUiModels = blockUiModels.toImmutableMap()
        )
    }

    val uiState = combine(
        groupFlow,
        dateFlow,
        placeFlow
    ) { groupModel, dateModel, placeModel ->
        PlanUiState(
            groups = groupModel.groups,
            selectedGroup = groupModel.selectedGroup,
            places = placeModel.places,
            blocks = placeModel.blocks,
            blockUiModels = placeModel.blockUiModels,
            date = dateModel
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlanUiState()
    )

    private val _event = Channel<PlanEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    private val originPlaces = uiState.value.places

    fun onAction(action: PlanAction) {
        when (action) {
            is PlanAction.BlockMoved -> {
                Log.d("DEBUG TEST", "move before : ${uiState.value.blocks}")
                moveBlock(action.id, action.newStartMinute)
                Log.d("DEBUG TEST", "move after : ${uiState.value.blocks}")
            }

            PlanAction.AddDay -> {
                dateFlow.update { it.copy(endDay = it.endDay?.plusDays(1)) }
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

                        dateFlow.update {
                            it.copy(
                                startDay = newStart,
                                endDay = newEnd,
                                currentDay = newCurrent,
                                longClickedDay = null
                            )
                        }
                        blockUiModelsFlow.update { adjustedUiModels }
                        blocksFlow.update { newBlocks }
                        placesFlow.update { (uiState.value.places + value).toImmutableList() }
                    }
                )
            }


            is PlanAction.LongClick -> {
                dateFlow.update { it.copy(longClickedDay = action.day) }
            }

            is PlanAction.SelectDay -> {
                dateFlow.update {
                    it.copy(currentDay = it.currentDayFromSelectedDay(selectedDay = action.day))
                }
            }

            PlanAction.RemoveDayClick -> {
                _event.trySend(element = ShowDeleteDayDialog(day = uiState.value.date.longClickedDay))
            }

            PlanAction.RemoveCancel -> {
                dateFlow.update { it.copy(longClickedDay = null) }
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

                dateFlow.update {
                    it.copy(
                        startDay = action.start,
                        endDay = action.end,
                        currentDay = action.start
                    )
                }
            }

            is PlanAction.DayScrolled -> {
                dateFlow.update {
                    it.copy(currentDay = it.currentDayFromSelectedDay(selectedDay = action.day))
                }
            }

            is PlanAction.ItemDragEnd -> {
                val date = uiState.value.date
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
                        rangeBaseDay.atStartOfDay().plusMinutes(it.endMinute.toLong())
                    startDateTime in startRange..endRange.minusMinutes(1) || endDateTime in startRange.plusMinutes(
                        1
                    )..endRange
                }
                if (isDuplicated) {
                    snackBarManager.show(SnackBarEvent.PLAN_INVALID_ERROR)
                    return
                }

                val newPlace = action.item.copy(
                    startDateTime = startDateTime,
                    endDateTime = endDateTime
                )
                blockUiModelsFlow.update { it + (action.item.id to newPlace) }
                blocksFlow.update { it + newPlace.toTimeBlock(dayStart = uiState.value.date.startDay!!.atStartOfDay())!! }
                placesFlow.update { (it - action.item).toImmutableList() }
            }

            PlanAction.GroupChoiceClick -> {
                _event.trySend(element = PlanEvent.ShowGroupChoiceDialog)
            }

            is PlanAction.GroupChoiceConfirmClick -> {
                selectedGroupFlow.update { action.selectedGroup }
                viewModelScope.launch {
                    placeRepository.getPlaceByGroupId(
                        groupId = action.selectedGroup.id,
                        page = 0,
                        size = 20
                    )
                        .onSuccess { result -> placesFlow.update { result.map { it.toUiModel() } } }
                        .onFailure { snackBarManager.show(SnackBarEvent.NETWORK_ERROR) }
                }
            }
            PlanAction.ShowCalendarClick -> {
                _event.trySend(element = PlanEvent.ShowCalendarDialog)
            }
        }
    }

    private fun moveBlock(id: String, newStartMinute: Int) {
        blocksFlow.update {
            val target = uiState.value.blocks.find { it.id == id } ?: return@update it
            Log.d("DEBUG TEST", "target : $target")
            if (!target.canMoveTo(
                    newStartMinute = newStartMinute,
                    blocks = uiState.value.blocks,
                    totalMinutes = uiState.value.date.totalMinutes
                )
            ) {
                return@update it
            }

            uiState.value.blocks.map { block ->
                Log.d("DEBUG TEST", "block : $block")
                if (block.id == id) {

                    Log.d("DEBUG TEST", "moved to call")
                    Log.d(
                        "DEBUG TEST",
                        "newStartMinute: $newStartMinute / totalMinutes: ${uiState.value.date.totalMinutes}"
                    )
                    block.movedTo(newStartMinute, uiState.value.date.totalMinutes)
                } else {
                    block
                }
            }
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
