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
import com.andone.memorip.domain.model.TimeBlock
import com.andone.memorip.domain.model.Visibility
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
import com.andone.memorip.presentation.util.DummyData.place
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
import kotlinx.coroutines.flow.onStart
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
    private val groupsFlow = MutableStateFlow(value = emptyList<GroupListUiModel>())
    private val groupUiStateFlow =
        combine(selectedGroupFlow, groupsFlow) { selectedGroup, groupsFlow ->
            PlanGroupUiModel(
                selectedGroup = selectedGroup,
                groups = groupsFlow.toImmutableList()
            )
        }

    private val placesFlow = MutableStateFlow(value = emptyList<Place>())
    private val timeBlocksFlow = MutableStateFlow(value = emptyList<TimeBlock>())
    private val selectedDateFlow = MutableStateFlow(value = DateUiModel())
    private val blockUiModelsFlow = MutableStateFlow(value = emptyMap<String, PlanBlockUiModel>())
    private val planPlaceUiStateFlow = combine(
        placesFlow,
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
    }.onStart {
        groupRepository.getSimpleGroups()
            .onSuccess { response ->
                if (response.isNotEmpty()) {
                    val defaultGroup = response.first()
                    updateSelectedGroup(GroupListUiModel.from(defaultGroup))
                    updatePlaces(groupId = defaultGroup.id)
                }
                groupsFlow.update { response.map { GroupListUiModel.from(it) } }
            }
            .onFailure { snackBarManager.show(event = SnackBarEvent.NETWORK_ERROR) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlanUiState()
    )

    private val _event = Channel<PlanEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    private var originPlaces: List<Place> = uiState.value.places

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
                        placesFlow.update { (it + value).toImmutableList() }
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
                updateGroup(startAt = action.start, endAt = action.end)
                updatePlaces(groupId = uiState.value.selectedGroup!!.id)
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
                saveGroup(uiState.value.selectedGroup)
                updateSelectedGroup(action.selectedGroup)
                updatePlaces(groupId = action.selectedGroup.id)
            }

            PlanAction.ShowCalendarClick -> {
                _event.trySend(element = PlanEvent.ShowCalendarDialog)
            }
        }
    }

    fun savePlan() {
        viewModelScope.launch {
            uiState.value.blocks.forEach { block ->
                val target = originPlaces.find { it.id == block.id }
                val originStartAt = target?.startDateTime
                val originEndAt = target?.endDateTime

                val startAtDate =
                    uiState.value.date.startDay!!.atStartOfDay().plusDays((block.day - 1).toLong())
                val startAt =
                    startAtDate.plusMinutes((block.startMinute - (block.day - 1) * MINUTES_PER_DAY).toLong())
                val endAtDate = uiState.value.date.startDay!!.atStartOfDay().plusDays((block.day - 1).toLong())
                val endAt = endAtDate.plusMinutes((block.startMinute + block.durationMinute - (block.day - 1) * MINUTES_PER_DAY).toLong())

                if (!(startAt.isEqual(originStartAt) && endAt.isEqual(originEndAt))) {
                    val startAtString = startAt.toRemoteString()
                    val endAtString = endAt.toRemoteString()

                    pendingUpdates[block.id] = Payload.PlaceTimeEditPayload(
                        startAt = startAtString,
                        endAt = endAtString
                    )
                    groupRepository.updatePlaceTime(
                        groupPlaceId = block.id,
                        startAt = startAtString!!,
                        endAt = endAtString!!
                    ).onSuccess {
                        pendingUpdates.remove(block.id)
                    }.onFailure {
                        if (it is CancellationException) { throw it }
                        else { snackBarManager.show(SnackBarEvent.NETWORK_ERROR) }
                    }
                }
            }
        }
    }

    private fun updatePlaces(groupId: String) {
        viewModelScope.launch {
            groupRepository.getPlaceByGroupId(groupId = groupId)
                .onSuccess { result ->
                    originPlaces = result.map { it.toUiModel() }
                    if (uiState.value.date.startDay != null && uiState.value.date.endDay != null) {
                        placesFlow.update {
                            val places = result.map { it.toUiModel() }
                            val noTimePlaces =
                                places.filter { it.startDateTime == null || it.endDateTime == null }
                            val validPlaces =
                                places.filter { it.startDateTime != null && it.endDateTime != null }
                            val outOfDatePlaces = validPlaces.filter {
                                it.startDateTime!!.toLocalDate() !in uiState.value.date.startDay!!..uiState.value.date.endDay!!
                            }.map {
                                it.copy(startDateTime = null, endDateTime = null)
                            }
                            val inDatePlaces = validPlaces.filter {
                                it.startDateTime!!.toLocalDate() in uiState.value.date.startDay!!..uiState.value.date.endDay!!
                            }.map {
                                val limitTime = uiState.value.date.endDay!!.atStartOfDay()
                                if (it.endDateTime!!.isAfter(limitTime)) {
                                    it.copy(endDateTime = limitTime)
                                } else {
                                    it
                                }
                            }

                            val timeBlocks =
                                inDatePlaces.mapNotNull { it.toTimeBlock(uiState.value.date.startDay?.atStartOfDay()!!) }
                            val uiBlocks = inDatePlaces.associateBy { it.id }
                            timeBlocksFlow.update { timeBlocks }
                            blockUiModelsFlow.update { uiBlocks }
                            (noTimePlaces + outOfDatePlaces).distinct()
                        }
                    }
                }
                .onFailure { snackBarManager.show(SnackBarEvent.NETWORK_ERROR) }

        }
    }

    private fun updateSelectedGroup(group: GroupListUiModel) {
        selectedGroupFlow.update { group }
        selectedDateFlow.update {
            DateUiModel(
                startDay = group.startDate,
                endDay = group.endDate,
                currentDay = group.startDate
            )
        }
    }

    private fun updateGroup(startAt: LocalDate, endAt: LocalDate) {
        selectedGroupFlow.update {
            it?.copy(
                startDate = startAt,
                endDate = endAt
            )
        }

        groupsFlow.update { groups ->
            groups.map {
                if (it.id == selectedGroupFlow.value!!.id) {
                    it.copy(startDate = startAt, endDate = endAt)
                } else {
                    it
                }
            }
        }
    }

    private fun saveGroup(targetGroup: GroupListUiModel?) {
        targetGroup?.let { group ->
            viewModelScope.launch {
                groupRepository.updateGroup(
                    groupId = group.id,
                    title = group.title,
                    startDate = group.startDate.toRemoteString(),
                    endDate = group.endDate.toRemoteString(),
                    /** TODO visibility 정보가 없어서 저장이 어려움 */
                    visibility = Visibility.PRIVATE
                )
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
        placesFlow.update { (it - place).toImmutableList() }
    }

    private fun buildPendingUpdateWork(id: String, payload: Payload): OneTimeWorkRequest {
        val payload = payload as Payload.PlaceTimeEditPayload
        val data = workDataOf(
            PlanWorker.ID to id,
            PlanWorker.START_AT to payload.startAt,
            PlanWorker.END_AT to payload.endAt
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

    override fun onCleared() {
        pendingUpdates.forEach { (id, update) ->
            val request = buildPendingUpdateWork(id, update)

            workManager.enqueueUniqueWork(
                uniqueWorkName = WORK_NAME + id,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                request
            )
        }
        super.onCleared()
    }

    companion object {
        const val WORK_NAME = "PLACE_TIME_EDIT"
    }
}