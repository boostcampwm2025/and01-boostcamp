package com.andone.memorip.presentation.screen.selecttrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.TripRepository
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripAction
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripAction.OnAddTripClick
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripAction.OnBackClick
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripAction.OnCheckClick
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripAction.OnDialogCancelClick
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripAction.OnDialogConfirmClick
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripAction.OnFABClick
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripAction.OnTripClick
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripEvent
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripUiModel
import com.andone.memorip.presentation.screen.selecttrip.model.SelectTripUiState
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectTripViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val placeRepository: PlaceRepository,
    private val snackBarManager: SnackBarManager
) : ViewModel() {

    private var placeId: String? = null
    private var initialSelectedTripId: String? = null

    private val _uiState = MutableStateFlow(SelectTripUiState())
    val uiState: StateFlow<SelectTripUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SelectTripUiState(isLoading = true)
        )

    private val _event = Channel<SelectTripEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    fun setPlaceId(placeId: String?) {
        // placeId가 변경되었거나, 같은 placeId로 다시 진입한 경우에도 초기화
        // (이전 선택 상태를 유지하지 않고 항상 API에서 최신 데이터를 가져옴)
        this.placeId = placeId
        // placeId가 있으면 initialSelectedTripId 초기화 (PlaceDetailScreen용)
        if (placeId != null) {
            this.initialSelectedTripId = null
        }
        resetAndFetchTrips()
    }

    fun setInitialSelectedTripId(tripId: String?) {
        this.initialSelectedTripId = tripId
    }

    /**
     * 상태를 초기화하고 API에서 최신 데이터를 조회
     * 화면이 나타날 때마다 호출되어 이전 선택 상태를 초기화함
     */
    fun resetAndFetchTrips() {
        // 상태 초기화
        _uiState.update {
            SelectTripUiState(
                trips = emptyList<SelectTripUiModel>().toImmutableList(),
                selectedTripIds = persistentSetOf(),
                initialSelectedTripIds = persistentSetOf(),
                isLoading = true
            )
        }
        fetchInitialTrips()
    }

    fun onAction(action: SelectTripAction) {
        when (action) {
            is SelectTripAction.OnInitialize -> {
                setPlaceId(action.placeId)
                if (action.placeId == null) {
                    setInitialSelectedTripId(action.initialSelectedTripId)
                }
            }

            OnFABClick -> {
                _event.trySend(element = SelectTripEvent.ShowDialog)
            }

            is OnTripClick -> {
                toggleTripSelection(action.trip.id)
            }

            OnAddTripClick -> {
                _event.trySend(element = SelectTripEvent.ShowDialog)
            }

            OnBackClick -> {
                _event.trySend(element = SelectTripEvent.NavigateBack)
            }

            OnCheckClick -> {
                if (placeId != null) {
                    // PlaceDetailScreen: addTripIds와 removeTripIds 둘 다 계산
                    confirmTripSelection()
                } else {
                    // PlaceCreateContainer: addTripIds만 전송 (초기 선택이 없으므로)
                    val currentState = _uiState.value
                    val selectedTripIds = currentState.selectedTripIds.toList()

                    if (selectedTripIds.isNotEmpty()) {
                        viewModelScope.launch {
                            val selectedTrip = currentState.trips.firstOrNull {
                                it.id in currentState.selectedTripIds
                            }
                            if (selectedTrip != null) {
                                _event.trySend(element = SelectTripEvent.SelectTrip(trip = selectedTrip))
                            }
                        }
                    }
                }
            }

            is OnDialogConfirmClick -> {
                addTrip(tripName = action.tripName)
            }

            OnDialogCancelClick -> {
                _event.trySend(element = SelectTripEvent.DismissDialog)
            }
        }
    }

    private fun toggleTripSelection(tripId: String) {
        _uiState.update { current ->
            val newSelectedIds = if (tripId in current.selectedTripIds) {
                current.selectedTripIds.filter { it != tripId }.toImmutableSet()
            } else {
                (current.selectedTripIds + tripId).toImmutableSet()
            }
            current.copy(selectedTripIds = newSelectedIds)
        }
    }

    private fun confirmTripSelection() {
        if (placeId == null) return

        val currentState = _uiState.value
        val initialSelectedIds = currentState.initialSelectedTripIds
        val currentSelectedIds = currentState.selectedTripIds

        val addTripIds = (currentSelectedIds - initialSelectedIds).toList()
        val removeTripIds = (initialSelectedIds - currentSelectedIds).toList()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            placeRepository.updatePlaceTrips(
                placeId = placeId!!,
                addTripIds = addTripIds,
                removeTripIds = removeTripIds
            )
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            initialSelectedTripIds = currentSelectedIds
                        )
                    }
                    snackBarManager.show(SnackBarEvent.SUCCESS)
                    _event.trySend(SelectTripEvent.PlaceTripsUpdated)
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    snackBarManager.show(SnackBarEvent.NETWORK_ERROR)
                }
        }
    }

    private fun fetchInitialTrips() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (placeId != null) {
                // PlaceDetail 화면: API에서 최신 데이터를 조회하여 초기 상태 설정
                // 항상 API에서 받아온 값으로 initialSelectedTripIds를 설정
                tripRepository.fetchMyTripsWithPlaceStatus(placeId = placeId)
                    .onSuccess { tripsWithPlaceAdded ->
                        val trips = tripsWithPlaceAdded.map { SelectTripUiModel.from(it) }
                            .toImmutableList()
                        // API에서 받아온 isPlaceAdded = true인 그룹들을 초기 선택 상태로 설정
                        val initiallySelectedIds = trips
                            .filter { it.isPlaceAdded }
                            .map { it.id }
                            .toImmutableSet()

                        _uiState.update { current ->
                            current.copy(
                                trips = trips,
                                selectedTripIds = initiallySelectedIds,
                                // API에서 조회한 값으로 initialSelectedTripIds 설정
                                // 이후 사용자가 변경한 내용과 비교하기 위해 사용
                                initialSelectedTripIds = initiallySelectedIds,
                                isLoading = false
                            )
                        }
                    }
                    .onFailure {
                        _uiState.update { it.copy(isLoading = false) }
                    }
            } else {
                tripRepository.fetchMyTrips()
                    .onSuccess {
                        val trips = tripRepository.myTrips.first()
                        val initiallySelectedIds = if (initialSelectedTripId != null) {
                            persistentSetOf(initialSelectedTripId!!)
                        } else {
                            persistentSetOf()
                        }
                        _uiState.update { current ->
                            current.copy(
                                trips = trips.map { SelectTripUiModel.from(it) }
                                    .toImmutableList(),
                                selectedTripIds = initiallySelectedIds,
                                initialSelectedTripIds = initiallySelectedIds,
                                isLoading = false
                            )
                        }
                    }
                    .onFailure {
                        _uiState.update { it.copy(isLoading = false) }
                    }
            }
        }
    }

    private fun addTrip(tripName: String) {
        viewModelScope.launch {
            // TODO: 실제 로그인한 사용자의 ID로 교체해야 합니다. 지금은 백엔드 자동 삽입 방식임.
            val ownerId = "current_user_id"

            _uiState.update { it.copy(isLoading = true) }

            // todo: visibility 받는 걸로 수정
            tripRepository.createTrip(
                ownerId = ownerId,
                title = tripName,
                visibility = Visibility.PUBLIC
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _event.trySend(SelectTripEvent.DismissDialog)
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                _event.trySend(SelectTripEvent.ShowSnackBar)
            }
        }
    }
}
