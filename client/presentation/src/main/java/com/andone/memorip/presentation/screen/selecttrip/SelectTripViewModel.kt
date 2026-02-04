package com.andone.memorip.presentation.screen.selecttrip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.domain.repository.TripRepository
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
    private var initialSelectedTripIds: List<String>? = null
    private var isPlaceMine: Boolean = true

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
        this.placeId = placeId
        if (placeId != null) {
            this.initialSelectedTripIds = null
        }
        resetAndFetchTrips()
    }

    fun setInitialSelectedTripIds(tripIds: List<String>?) {
        this.initialSelectedTripIds = tripIds
    }

    fun resetAndFetchTrips() {
        _uiState.update {
            SelectTripUiState(
                trips = emptyList<SelectTripUiModel>().toImmutableList(),
                selectedTripIds = persistentSetOf(),
                initialSelectedTripIds = persistentSetOf(),
                isPlaceMine = isPlaceMine,
                isLoading = true
            )
        }
        fetchInitialTrips()
    }

    fun onAction(action: SelectTripAction) {
        when (action) {
            is SelectTripAction.OnInitialize -> {
                isPlaceMine = action.isPlaceMine
                setPlaceId(action.placeId)
                if (action.placeId == null) {
                    setInitialSelectedTripIds(action.initialSelectedTripIds)
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
                    confirmTripSelection()
                } else {
                    val currentState = _uiState.value
                    val selectedTripIds = currentState.selectedTripIds.toList()

                    if (selectedTripIds.isNotEmpty()) {
                        viewModelScope.launch {
                            val selectedTrips =
                                currentState.trips.filter { it.id in currentState.selectedTripIds }
                            if (selectedTrips.isNotEmpty()) {
                                _event.trySend(element = SelectTripEvent.SelectTrip(trips = selectedTrips))
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
        val currentState = _uiState.value

        if (currentState.isPlaceMine &&
            tripId in currentState.selectedTripIds &&
            currentState.selectedTripIds.size == 1
        ) {
            snackBarManager.show(SnackBarEvent.PLACE_MUST_HAVE_ONE_TRIP)
            return
        }

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
                tripRepository.fetchMyTripsWithPlaceStatus(placeId = placeId)
                    .onSuccess { tripsWithPlaceAdded ->
                        val trips = tripsWithPlaceAdded.map { SelectTripUiModel.from(it) }
                            .toImmutableList()
                        val initiallySelectedIds = trips
                            .filter { it.isPlaceAdded }
                            .map { it.id }
                            .toImmutableSet()

                        _uiState.update { current ->
                            current.copy(
                                trips = trips,
                                selectedTripIds = initiallySelectedIds,
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
                        val initiallySelectedIds = if (initialSelectedTripIds != null) {
                            initialSelectedTripIds!!
                        } else {
                            persistentSetOf()
                        }
                        _uiState.update { current ->
                            current.copy(
                                trips = trips.map { SelectTripUiModel.from(it) }
                                    .toImmutableList(),
                                selectedTripIds = initiallySelectedIds.toImmutableSet(),
                                initialSelectedTripIds = initiallySelectedIds.toImmutableSet(),
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
            _uiState.update { it.copy(isLoading = true) }

            tripRepository.createTrip(
                title = tripName,
                visibility = Visibility.PUBLIC
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _event.trySend(SelectTripEvent.DismissDialog)
                fetchInitialTrips()
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                _event.trySend(SelectTripEvent.ShowSnackBar)
            }
        }
    }
}
