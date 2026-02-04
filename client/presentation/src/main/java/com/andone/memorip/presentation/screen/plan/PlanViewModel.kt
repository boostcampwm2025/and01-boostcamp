package com.andone.memorip.presentation.screen.plan

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.TripRepository
import com.andone.memorip.presentation.model.Payload
import com.andone.memorip.presentation.model.Payload.PlaceTimeEditPayload
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.PlanBlockUiModel
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.plan.PlanViewModelConstants.DAYS_LIMIT
import com.andone.memorip.presentation.screen.plan.model.DateUiModel
import com.andone.memorip.presentation.screen.plan.model.PlanAction
import com.andone.memorip.presentation.screen.plan.model.PlanEvent
import com.andone.memorip.presentation.screen.plan.model.PlanEvent.ShowDeleteDayDialog
import com.andone.memorip.presentation.screen.plan.model.PlanTripUiModel
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.screen.plan.model.TripListUiModel
import com.andone.memorip.presentation.screen.plan.utill.MINUTES_PER_DAY
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import com.andone.memorip.presentation.util.toRemoteString
import com.andone.memorip.presentation.util.workmanager.PlanWorker
import com.andone.memorip.presentation.util.workmanager.TripWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
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
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
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
    private val tripRepository: TripRepository,
    private val snackBarManager: SnackBarManager,
    private val workManager: WorkManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val selectedTripFlow = MutableStateFlow<TripListUiModel?>(value = null)
    private val tripsFlow = MutableStateFlow(value = emptyList<TripListUiModel>())
    private val tripUiStateFlow =
        combine(selectedTripFlow, tripsFlow) { selectedTrip, tripsFlow ->
            PlanTripUiModel(
                selectedTrip = selectedTrip,
                trips = tripsFlow.toImmutableList()
            )
        }

    private val placesFlow = MutableStateFlow(value = emptyList<Place>())
    private val selectedDateFlow = MutableStateFlow(value = DateUiModel())
    private val selectedBlockFlow = MutableStateFlow<PlanBlockUiModel?>(null)

    val uiState = combine(
        tripUiStateFlow,
        selectedDateFlow,
        placesFlow,
        selectedBlockFlow
    ) { tripUiState, dateUiState, placeState, selectedBlock ->
        PlanUiState(
            trips = tripUiState.trips,
            selectedTrip = tripUiState.selectedTrip,
            places = placeState.toImmutableList(),
            date = dateUiState,
            updatedBlock = selectedBlock
        )
    }.onStart {
        tripRepository.getSimpleTrips()
            .onSuccess { response ->
                if (response.isNotEmpty()) {
                    val selectedTripId = savedStateHandle.get<String>(SELECTED_TRIP_ID)
                    val defaultTrip =
                        if (selectedTripId == null) response.first() else response.find { it.id == selectedTripId }
                            ?: response.first()
                    updateSelectedTrip(TripListUiModel.from(defaultTrip))
                    updatePlaces(tripId = defaultTrip.id)
                }
                tripsFlow.update { response.map { TripListUiModel.from(it) } }
            }
            .onFailure { snackBarManager.show(event = SnackBarEvent.NETWORK_ERROR) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlanUiState()
    )

    private val _event = Channel<PlanEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    private var originPlaces: List<PlanBlockUiModel> = uiState.value.places

    private val pendingUpdates = mutableMapOf<String, Payload>()

    fun onAction(action: PlanAction) {
        when (action) {
            is PlanAction.BlockMoved -> {
                moveBlock(action.id, action.newStartMinute)
            }

            is PlanAction.BlockClick -> {
                val targetBlock = uiState.value.places.find { it.id == action.id } ?: return
                selectedBlockFlow.update { targetBlock }
            }

            is PlanAction.BlockSlide -> {
                deleteBlock(action.id)
            }

            is PlanAction.BottomBlockDragEnd -> {
                addPlaceToTimetable(startMinute = action.startMinute, place = action.item)
            }

            PlanAction.AddDayClick -> {
                selectedDateFlow.update { it.copy(endDay = it.endDay?.plusDays(1)) }
            }

            is PlanAction.RemoveDayDialogConfirmClick -> {
                deleteDay(action.day)
            }

            PlanAction.RemoveDayClick -> {
                _event.trySend(element = ShowDeleteDayDialog(day = uiState.value.date.longClickedDay))
            }

            is PlanAction.DayLongClick -> {
                selectedDateFlow.update { it.copy(longClickedDay = action.day) }
            }

            is PlanAction.DayClick -> {
                selectedDateFlow.update {
                    it.copy(currentDay = it.currentDayFromSelectedDay(selectedDay = action.day))
                }
            }

            PlanAction.RemoveDayCancelClick -> {
                selectedDateFlow.update { it.copy(longClickedDay = null) }
            }

            is PlanAction.SelectDateDialogConfirmClick -> {
                updateSelectedDate(action.start, action.end)
            }

            is PlanAction.DayScrolled -> {
                selectedDateFlow.update {
                    it.copy(currentDay = it.currentDayFromSelectedDay(selectedDay = action.day))
                }
            }

            PlanAction.TripChoiceClick -> {
                _event.trySend(element = PlanEvent.ShowTripChoiceDialog)
            }

            is PlanAction.TripChoiceDialogConfirmClick -> {
                savePlan()
                saveTrip(uiState.value.selectedTrip)
                updateSelectedTrip(action.selectedTrip)
                updatePlaces(tripId = action.selectedTrip.id)
                originPlaces = uiState.value.places
            }

            PlanAction.ShowCalendarClick -> {
                _event.trySend(element = PlanEvent.ShowCalendarDialog)
            }

            PlanAction.PlanEditCancelClick -> {
                selectedBlockFlow.update { null }
            }

            is PlanAction.PlanEditDialogConfirmClick -> {
                if (action.startDateTime.isEqual(action.endDateTime)) {
                    snackBarManager.show(SnackBarEvent.PLAN_DATE_INVALID_ERROR)
                    return
                }

                updatePlan(
                    id = action.id,
                    startDateTime = action.startDateTime,
                    endDateTime = action.endDateTime
                )
                selectedBlockFlow.update { null }
            }
        }
    }

    fun savePlan() {
        viewModelScope.launch {
            uiState.value.places.forEach { place ->
                when (place) {
                    is Place -> {
                        val target =
                            originPlaces.find { it.id == place.id } as Place? ?: return@launch
                        val originStartAt = target.startDateTime
                        val originEndAt = target.endDateTime

                        val startAt = place.startDateTime
                        val endAt = place.endDateTime

                        if (startAt != null && endAt != null && startAt != originStartAt || endAt != originEndAt) {
                            val startAtString = startAt.toRemoteString()
                            val endAtString = endAt.toRemoteString()

                            pendingUpdates[place.id] = PlaceTimeEditPayload(
                                startAt = startAtString,
                                endAt = endAtString
                            )
                            tripRepository.updatePlaceTime(
                                tripPlaceId = place.id,
                                startAt = startAtString!!,
                                endAt = endAtString!!
                            ).onSuccess {
                                pendingUpdates.remove(place.id)
                            }
                        }
                    }
                }
            }
            uiState.value.places.forEach { place ->
                when (place) {
                    is Place -> {
                        originPlaces.find { it.id == place.id }?.let { origin ->
                            val origin = origin as Place
                            if ((origin.startDateTime != null && origin.endDateTime != null) && (place.startDateTime == null && place.endDateTime == null)) {
                                pendingUpdates[place.id] = Payload.TripDeletePayload(place.id)
                                tripRepository.clearPlaceTime(tripPlaceId = place.id)
                                    .onSuccess {
                                        pendingUpdates.remove(place.id)
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    fun saveTrip(targetTrip: TripListUiModel?) {
        targetTrip?.let { trip ->
            viewModelScope.launch {
                pendingUpdates[trip.id] = Payload.TripSavePayload(
                    title = trip.title,
                    startAt = trip.startDate.toRemoteString(),
                    endAt = trip.endDate.toRemoteString()
                )
                tripRepository.updateTrip(
                    tripId = trip.id,
                    title = trip.title,
                    startDate = trip.startDate.toRemoteString(),
                    endDate = trip.endDate.toRemoteString(),
                    /** TODO visibility 정보가 없어서 저장이 어려움 */
                    visibility = Visibility.PRIVATE
                ).onSuccess {
                    pendingUpdates.remove(trip.id)
                }
            }
        }
    }

    private fun updatePlan(id: String, startDateTime: LocalDateTime, endDateTime: LocalDateTime) {
        val targetBlock = uiState.value.places.find { it.id == id } ?: return
        when (targetBlock) {
            is Place -> {
                placesFlow.update { places ->
                    places.map { place ->
                        if (targetBlock.id == place.id) {
                            if (isDuplicate(
                                    targetId = id,
                                    startDateTime = startDateTime,
                                    endDateTime = endDateTime
                                )
                            ) {
                                return@update places
                            }

                            place.copy(startDateTime = startDateTime, endDateTime = endDateTime)
                        } else {
                            place
                        }
                    }
                }
            }
        }
    }

    private fun updatePlaces(tripId: String) {
        viewModelScope.launch {
            tripRepository.getPlaceByTripId(tripId = tripId)
                .onSuccess { result ->
                    val places = result.map { it.toUiModel() }
                    placesFlow.update { places }
                    originPlaces = places
                }
                .onFailure { snackBarManager.show(SnackBarEvent.NETWORK_ERROR) }
        }
    }

    private fun updateSelectedTrip(trip: TripListUiModel) {
        selectedTripFlow.update { trip }
        selectedDateFlow.update {
            DateUiModel(
                startDay = trip.startDate,
                endDay = trip.endDate,
                currentDay = trip.startDate
            )
        }
    }

    private fun updateTrip(startAt: LocalDate, endAt: LocalDate) {
        selectedTripFlow.update {
            it?.copy(
                startDate = startAt,
                endDate = endAt
            )
        }

        tripsFlow.update { trips ->
            trips.map {
                if (it.id == selectedTripFlow.value!!.id) {
                    it.copy(startDate = startAt, endDate = endAt)
                } else {
                    it
                }
            }
        }
    }

    private fun updateSelectedDate(startAt: LocalDate, endAt: LocalDate) {
        val dayDiff = ChronoUnit.DAYS.between(startAt, endAt).days.toInt(DurationUnit.DAYS)

        if (dayDiff > DAYS_LIMIT) {
            snackBarManager.show(SnackBarEvent.PLAN_DAYS_VALIDATION_ERROR)
            return
        }

        selectedDateFlow.update {
            it.copy(
                startDay = startAt,
                endDay = endAt,
                currentDay = startAt
            )
        }
        updateTrip(startAt = startAt, endAt = endAt)
        updatePlaces(tripId = uiState.value.selectedTrip!!.id)
    }

    private fun moveBlock(id: String, newStartMinute: Int) {
        placesFlow.update {
            val target = uiState.value.places.find { it.id == id } ?: return@update it
            val startDateTime =
                uiState.value.date.startDay?.atStartOfDay()?.plusMinutes(newStartMinute.toLong())
                    ?: return@update it

            when (target) {
                is Place -> {
                    val endDateTime = uiState.value.date.startDay?.atStartOfDay()
                        ?.plusMinutes(newStartMinute + target.durationMinutes) ?: return@update it
                    if (isDuplicate(
                            targetId = id,
                            startDateTime = startDateTime,
                            endDateTime = endDateTime
                        )
                    ) {
                        return@update it
                    }

                    uiState.value.places.map { place ->
                        when (place) {
                            is Place -> {
                                if (place.id == id) {
                                    val duration = place.durationMinutes
                                    val startDateTime = uiState.value.date.startDay!!.atStartOfDay()
                                        .plusMinutes(newStartMinute.toLong())
                                    var endDateTime = startDateTime.plusMinutes(duration)

                                    if (endDateTime.isAfter(uiState.value.date.endDay!!.atStartOfDay())) {
                                        endDateTime = uiState.value.date.endDay!!.atStartOfDay()
                                    }

                                    place.copy(
                                        startDateTime = startDateTime,
                                        endDateTime = endDateTime
                                    )
                                } else {
                                    place
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun deleteBlock(id: String) {
        placesFlow.update { places ->
            places.map { place ->
                when (place) {
                    is Place -> {
                        if (place.id == id) {
                            place.copy(startDateTime = null, endDateTime = null)
                        } else {
                            place
                        }
                    }
                }
            }
        }
    }

    private fun deleteDay(day: Int) {
        val date = uiState.value.date
        val (newStart, newEnd) = date.deleteDay(dayIndex = day)

        val newCurrent = adjustCurrentDay(
            date = date,
            dayIndex = day,
            newStart = newStart,
            newEnd = newEnd
        )

        selectedDateFlow.update {
            it.copy(
                startDay = newStart,
                endDay = newEnd,
                currentDay = newCurrent,
                longClickedDay = null
            )
        }

        adjustBlockUiModelsAfterDayRemoved(
            places = uiState.value.places,
            removedDayIndex = day,
            date = uiState.value.date
        )
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
        places: List<PlanBlockUiModel>,
        removedDayIndex: Int,
        date: DateUiModel
    ) {
        val removedDate = date.currentDayFromSelectedDay(removedDayIndex)

        val removedIds = places
            .mapNotNull { place ->
                when (place) {
                    is Place -> {
                        if (removedDate != null) {
                            if (place.startDateTime?.toLocalDate() == removedDate) {
                                place.id
                            } else {
                                null
                            }
                        } else null
                    }
                }
            }
            .toSet()

        placesFlow.update { places ->
            places.map { originPlace ->
                when (originPlace) {
                    is Place -> {
                        val startDate = originPlace.startDateTime?.toLocalDate()
                        val endDateTime = originPlace.endDateTime

                        if (originPlace.id in removedIds) {
                            return@map originPlace.copy(startDateTime = null, endDateTime = null)
                        }

                        if (startDate == null || endDateTime == null) {
                            return@map originPlace
                        }

                        if (startDate.isAfter(removedDate)) {
                            return@map originPlace.copy(
                                startDateTime = originPlace.startDateTime.minusDays(
                                    1
                                ), endDateTime = originPlace.endDateTime.minusDays(1)
                            )
                        }

                        return@map originPlace
                    }
                }
            }
        }
    }

    private fun isDuplicate(
        targetId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): Boolean {
        val filteredPlaces = uiState.value.blockItems.filterNot { it.id == targetId }

        return filteredPlaces.any {
            it.startDateTime!! in startDateTime..endDateTime.minusMinutes(1)
                || (it.startDateTime!! < startDateTime && it.endDateTime!! > startDateTime)
        }
    }

    private fun addPlaceToTimetable(startMinute: Int, place: Place) {
        val date = uiState.value.date
        val baseDay = date.startDay ?: return

        val dayOffset = startMinute / MINUTES_PER_DAY
        val minuteInDay = startMinute % MINUTES_PER_DAY

        val targetDay = baseDay.plusDays(dayOffset.toLong())

        val startDateTime = targetDay.atStartOfDay()
            .plusMinutes(minuteInDay.toLong())

        val endDateTime = startDateTime.plusMinutes(place.durationMinutes)

        if (isDuplicate(
                targetId = place.id,
                startDateTime = startDateTime,
                endDateTime = endDateTime
            )
        ) {
            snackBarManager.show(SnackBarEvent.PLAN_INVALID_ERROR)
            return
        }

        placesFlow.update { places ->
            places.map { targetPlace ->
                if (targetPlace.id == place.id) {
                    targetPlace.copy(startDateTime = startDateTime, endDateTime = endDateTime)
                } else {
                    targetPlace
                }
            }
        }
    }

    private fun buildPendingUpdateWork(id: String, payload: Payload): OneTimeWorkRequest {
        return when (payload) {
            is Payload.PlaceTimeEditPayload -> {
                val data = workDataOf(
                    PlanWorker.ID to id,
                    PlanWorker.START_AT to payload.startAt,
                    PlanWorker.END_AT to payload.endAt
                )

                OneTimeWorkRequestBuilder<PlanWorker>()
                    .setInputData(data)
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        10,
                        TimeUnit.SECONDS
                    )
                    .build()
            }

            is Payload.TripSavePayload -> {
                val data = workDataOf(
                    TripWorker.ID to id,
                    TripWorker.TITLE to payload.title,
                    TripWorker.START_AT to payload.startAt,
                    TripWorker.END_AT to payload.endAt,
                    TripWorker.FLAG to TripWorker.UPDATE_FLAG
                )

                OneTimeWorkRequestBuilder<TripWorker>()
                    .setInputData(data)
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        10,
                        TimeUnit.SECONDS
                    )
                    .build()
            }

            is Payload.TripDeletePayload -> {
                val data = workDataOf(
                    TripWorker.ID to id,
                    TripWorker.FLAG to TripWorker.DELETE_FLAG
                )

                OneTimeWorkRequestBuilder<TripWorker>()
                    .setInputData(data)
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        10,
                        TimeUnit.SECONDS
                    )
                    .build()
            }
        }
    }

    override fun onCleared() {
        pendingUpdates.forEach { (id, payload) ->
            val request = buildPendingUpdateWork(id, payload)
            val name = when (payload) {
                is Payload.PlaceTimeEditPayload -> PLACE_WORK_NAME + id
                is Payload.TripDeletePayload -> PLACE_WORK_NAME + id
                is Payload.TripSavePayload -> TRIP_WORK_NAME + id
            }

            workManager.enqueueUniqueWork(
                uniqueWorkName = name,
                ExistingWorkPolicy.APPEND_OR_REPLACE,
                request
            )
        }
        savedStateHandle[SELECTED_TRIP_ID] = selectedTripFlow.value?.id

        super.onCleared()
    }

    companion object {
        const val PLACE_WORK_NAME = "PLACE_TIME_EDIT"
        const val TRIP_WORK_NAME = "TRIP_TIME_SAVE"
        const val SELECTED_TRIP_ID = "SELECTED_TRIP"
    }
}