package com.andone.memorip.presentation.screen.plan

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.andone.memorip.domain.model.GroupListItem
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.domain.repository.GroupRepository
import com.andone.memorip.presentation.model.Payload
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
import com.andone.memorip.presentation.util.toRemoteString
import com.andone.memorip.presentation.util.workmanager.PlanWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

private object PlanViewModelConstants {
    const val DAYS_LIMIT = 30
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PlanViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val snackBarManager: SnackBarManager,
    private val workManager: WorkManager
) : ViewModel() {
    private val selectedGroupFlow = MutableStateFlow<GroupListUiModel?>(value = null)
    private val remoteGroupsFlow = flow {
        var result = emptyList<GroupListItem>()
        groupRepository.getSimpleGroups()
            .onSuccess { response ->
                result = response
                if (response.isNotEmpty()) selectedGroupFlow.update { GroupListUiModel.from(response.first()) }
            }
            .onFailure { snackBarManager.show(event = SnackBarEvent.NETWORK_ERROR) }
        emit(result)
    }
    private val groupUiStateFlow =
        combine(selectedGroupFlow, remoteGroupsFlow) { selectedGroup, remoteGroupsFlow ->
            PlanGroupUiModel(
                selectedGroup = selectedGroup,
                groups = remoteGroupsFlow.map { GroupListUiModel.from(it) }.toImmutableList()
            )
        }

    private val remotePlacesByGroupFlow = selectedGroupFlow.flatMapLatest { group ->
        flow {
            var result = emptyList<Place>()
            if (group != null) {
                groupRepository.getPlaceByGroupId(group.id)
                    .onSuccess { success -> result = success.map { it.toUiModel() } }
                    .onFailure { snackBarManager.show(SnackBarEvent.NETWORK_ERROR) }
            }
            emit(result)
        }
    }
    private val localDeletedPlacesFlow = MutableStateFlow(value = emptyList<Place>())
    private val visiblePlacesFlow =
        combine(remotePlacesByGroupFlow, localDeletedPlacesFlow) { origin, deleted ->
            origin.filter { it !in deleted }
        }
    private val timeBlocksFlow = MutableStateFlow(value = emptyList<TimeBlock>())
    private val selectedDateFlow = MutableStateFlow(value = DateUiModel())
    private val blockUiModelsFlow = MutableStateFlow(value = emptyMap<String, PlanBlockUiModel>())
    private val planPlaceUiStateFlow = combine(
        visiblePlacesFlow,
        timeBlocksFlow,
        blockUiModelsFlow
    ) { places, blocks, blockUiModels ->
        PlanPlaceUiModel(
            places = places.toImmutableList(),
            blocks = blocks.toImmutableList(),
            blockUiModels = blockUiModels.toImmutableMap()
        )
    }

    val uiState = combine(
        groupUiStateFlow,
        selectedDateFlow,
        planPlaceUiStateFlow
    ) { groupUiState, dateUiState, planPlaceUiState ->
        PlanUiState(
            groups = groupUiState.groups,
            selectedGroup = groupUiState.selectedGroup,
            places = planPlaceUiState.places,
            blocks = planPlaceUiState.blocks,
            blockUiModels = planPlaceUiState.blockUiModels,
            date = dateUiState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlanUiState()
    )

    private val _event = Channel<PlanEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    private val originPlaces = uiState.value.places

    private val pendingUpdates = mutableMapOf<String, Payload>()

    fun onAction(action: PlanAction) {
        when (action) {
            is PlanAction.BlockMoved -> {
                moveBlock(action.id, action.newStartMinute)
            }

            PlanAction.AddDay -> {
                selectedDateFlow.update { it.copy(endDay = it.endDay?.plusDays(1)) }
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

                        selectedDateFlow.update {
                            it.copy(
                                startDay = newStart,
                                endDay = newEnd,
                                currentDay = newCurrent,
                                longClickedDay = null
                            )
                        }
                        blockUiModelsFlow.update { adjustedUiModels }
                        timeBlocksFlow.update { newBlocks }
                        localDeletedPlacesFlow.update { (it - value).toImmutableList() }
                    }
                )
            }


            is PlanAction.LongClick -> {
                selectedDateFlow.update { it.copy(longClickedDay = action.day) }
            }

            is PlanAction.SelectDay -> {
                selectedDateFlow.update {
                    it.copy(currentDay = it.currentDayFromSelectedDay(selectedDay = action.day))
                }
            }

            PlanAction.RemoveDayClick -> {
                _event.trySend(element = ShowDeleteDayDialog(day = uiState.value.date.longClickedDay))
            }

            PlanAction.RemoveCancel -> {
                selectedDateFlow.update { it.copy(longClickedDay = null) }
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

                selectedDateFlow.update {
                    it.copy(
                        startDay = action.start,
                        endDay = action.end,
                        currentDay = action.start
                    )
                }
            }

            is PlanAction.DayScrolled -> {
                selectedDateFlow.update {
                    it.copy(currentDay = it.currentDayFromSelectedDay(selectedDay = action.day))
                }
            }

            is PlanAction.ItemDragEnd -> {
                addPlaceToTimetable(startMinute = action.startMinute, place = action.item)
            }

            PlanAction.GroupChoiceClick -> {
                _event.trySend(element = PlanEvent.ShowGroupChoiceDialog)
            }

            is PlanAction.GroupChoiceConfirmClick -> {
                savePlan()
                selectedGroupFlow.update { action.selectedGroup }
            }

            PlanAction.ShowCalendarClick -> {
                _event.trySend(element = PlanEvent.ShowCalendarDialog)
            }
        }
    }

    private fun moveBlock(id: String, newStartMinute: Int) {
        timeBlocksFlow.update {
            val target = uiState.value.blocks.find { it.id == id } ?: return@update it
            if (!target.canMoveTo(
                    newStartMinute = newStartMinute,
                    blocks = uiState.value.blocks,
                    totalMinutes = uiState.value.date.totalMinutes
                )
            ) {
                return@update it
            }

            uiState.value.blocks.map { block ->
                if (block.id == id) {
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

    private fun addPlaceToTimetable(startMinute: Int, place: Place) {
        val date = uiState.value.date
        val baseDay = date.startDay ?: return

        val totalMinute = startMinute

        val dayOffset = totalMinute / MINUTES_PER_DAY
        val minuteInDay = totalMinute % MINUTES_PER_DAY

        val targetDay = baseDay.plusDays(dayOffset.toLong())

        val startDateTime = targetDay.atStartOfDay()
            .plusMinutes(minuteInDay.toLong())

        val endDateTime = startDateTime.plusMinutes(place.durationMinutes)

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

        val newPlace = place.copy(
            startDateTime = startDateTime,
            endDateTime = endDateTime
        )
        blockUiModelsFlow.update { it + (place.id to newPlace) }
        timeBlocksFlow.update { it + newPlace.toTimeBlock(dayStart = uiState.value.date.startDay!!.atStartOfDay())!! }
        localDeletedPlacesFlow.update { (it + place).toImmutableList() }
    }

    fun savePlan() {
        Log.d("DEBUG TEST", "save plan call")
        viewModelScope.launch {
            Log.d("DEBUG TEST", "block ui models : ${uiState.value.blockUiModels}")
            uiState.value.blockUiModels.forEach { (groupPlaceId, place) ->
                when (place) {
                    is Place -> {
                        if (place != uiState.value.places.find { it.id == groupPlaceId }) {
                            val startAt = place.startDateTime.toRemoteString()
                            val endAt = place.endDateTime.toRemoteString()
                            pendingUpdates[groupPlaceId] = Payload.PlaceTimeEditPayload(
                                startAt = startAt,
                                endAt = endAt
                            )
                            groupRepository.updatePlaceTime(
                                groupPlaceId = groupPlaceId,
                                startAt = startAt,
                                endAt = endAt
                            ).onSuccess {
                                Log.d("DEBUG TEST", "edit success")
                                pendingUpdates.remove(groupPlaceId)
                            }
                                .onFailure {
                                    if (it is CancellationException) throw it
                                    Log.d("DEBUG TEST", "edit failure")
                                    snackBarManager.show(SnackBarEvent.NETWORK_ERROR)
                                }
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onCleared() {
        Log.d("DEBUG TEST", "onCleared call")
        pendingUpdates.forEach { (id, update) ->
            Log.d("DEBUG TEST", "pending update $id : $update")
            val request = buildPendingUpdateWork(id, update)

            workManager.enqueueUniqueWork(
                uniqueWorkName = WORK_NAME + id,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                request
            )
        }
        super.onCleared()
    }

    private fun buildPendingUpdateWork(id: String, payload: Payload): OneTimeWorkRequest {
        val payload = payload as Payload.PlaceTimeEditPayload
        val data = workDataOf(
            PlanWorker.ID to id,
            PlanWorker.START_AT to payload.startAt,
            PlanWorker.END_AT to payload.startAt
        )

        return OneTimeWorkRequestBuilder<PlanWorker>()
            .setInputData(data)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                10,
                TimeUnit.SECONDS
            )
            .build()
    }

    companion object {
        const val WORK_NAME = "PLACE_TIME_EDIT"
    }
}