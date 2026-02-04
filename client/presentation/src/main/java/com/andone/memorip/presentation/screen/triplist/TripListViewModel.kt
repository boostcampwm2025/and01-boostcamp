package com.andone.memorip.presentation.screen.triplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.TripRepository
import com.andone.memorip.presentation.screen.triplist.model.TripListAction
import com.andone.memorip.presentation.screen.triplist.model.TripListEvent
import com.andone.memorip.presentation.screen.triplist.model.TripListUiState
import com.andone.memorip.presentation.model.TripUiModel
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val snackBarManager: SnackBarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripListUiState())
    val uiState: StateFlow<TripListUiState> = _uiState
        .onStart {
            observeTrips()
            fetchInitialTrips()
            observeSearchQuery()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TripListUiState(isLoading = true)
        )

    private val _event = Channel<TripListEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    /**
     * Repository의 여행 목록을 관찰하여 UI 상태 업데이트
     */
    private fun observeTrips() {
        viewModelScope.launch {
            tripRepository.myTrips.collect { trips ->
                _uiState.update { current ->
                    current.copy(trips = trips.map { TripUiModel.from(it) }.toImmutableList())
                }
            }
        }
    }

    /**
     * 검색어를 관찰하고 debounce 적용
     * 사용자가 입력을 멈춘 후 300ms가 지나면 서버에 검색 요청
     */
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _uiState
                .map { it.searchQuery } // searchQuery만 추출
                .distinctUntilChanged() // 값이 변경될 때만 emit
                .debounce(300) // 300ms 동안 입력이 없으면 실행
                .collect { query ->
                    fetchTripsWithQuery(query)
                }
        }
    }

    /**
     * 검색어와 함께 여행 목록을 서버에서 가져오기
     */
    private fun fetchTripsWithQuery(query: String) {
        viewModelScope.launch {
            val queryParam = query.ifEmpty { null }
            tripRepository.fetchMyTrips(query = queryParam)
                .onFailure {
                    snackBarManager.show(SnackBarEvent.NETWORK_ERROR)
                }
        }
    }

    private fun fetchInitialTrips() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            tripRepository.fetchMyTrips()
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    snackBarManager.show(SnackBarEvent.NETWORK_ERROR)
                }
        }
    }

    fun onAction(action: TripListAction) {
        when (action) {
            TripListAction.OnFABClick -> {
                _event.trySend(TripListEvent.NavigateToPlaceCreate)
            }

            is TripListAction.OnTripClick -> {
                _event.trySend(TripListEvent.NavigateToTripDetail(tripId = action.tripId))
            }

            TripListAction.OnCreateNewTripClick -> {
                _uiState.update {
                    it.copy(
                        isCreateTripDialogVisible = true,
                        newTripName = ""
                    )
                }
            }

            is TripListAction.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = action.query) }
            }

            is TripListAction.OnScrollStateChange -> {
                _uiState.update {
                    it.copy(isStickyHeaderVisible = !action.isScrollingDown)
                }
            }

            TripListAction.OnDismissCreateTripDialog -> {
                _uiState.update {
                    it.copy(
                        isCreateTripDialogVisible = false,
                        newTripName = ""
                    )
                }
            }

            is TripListAction.OnNewTripNameChange -> {
                _uiState.update { it.copy(newTripName = action.name) }
            }

            TripListAction.OnConfirmCreateTrip -> {
                createTrip()
            }
        }
    }

    private fun createTrip() {
        val tripName = _uiState.value.newTripName.trim()
        if (tripName.isEmpty()) {
            return
        }

        viewModelScope.launch {
            tripRepository.createTrip(
                title = tripName,
                visibility = Visibility.PRIVATE
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isCreateTripDialogVisible = false,
                        newTripName = ""
                    )
                }
                fetchInitialTrips()
            }.onFailure {
                _uiState.update {
                    it.copy(isCreateTripDialogVisible = false)
                }
                snackBarManager.show(SnackBarEvent.DATA_SAVE_FAILED)
            }
        }
    }
}
