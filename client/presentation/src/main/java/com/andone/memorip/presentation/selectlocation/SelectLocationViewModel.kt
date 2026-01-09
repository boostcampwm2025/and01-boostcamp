package com.andone.memorip.presentation.selectlocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.andone.memorip.domain.repository.KakaoSearchRepository
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.selectlocation.SelectLocationViewModelConstants.TIMEOUT_MILLS
import com.andone.memorip.presentation.selectlocation.model.SelectLocationAction
import com.andone.memorip.presentation.selectlocation.model.SelectLocationEvent
import com.andone.memorip.presentation.selectlocation.model.SelectLocationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private object SelectLocationViewModelConstants {
    const val TIMEOUT_MILLS = 500L
}

@HiltViewModel
class SelectLocationViewModel @Inject constructor(
    private val searchRepository: KakaoSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectLocationUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SelectLocationEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    @OptIn(FlowPreview::class)
    private val searchQueryFlow = _uiState
        .map { it.query }
        .distinctUntilChanged()
        .debounce(TIMEOUT_MILLS)

    @OptIn(ExperimentalCoroutinesApi::class)
    val locationsPagingFlow: Flow<PagingData<LocationUiModel>> = searchQueryFlow
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(PagingData.empty())
            } else {
                searchRepository.searchLocations(query)
                    .map { pagingData -> pagingData.map { dto -> dto.toUiModel() } }
            }
        }
        .cachedIn(viewModelScope)

    fun onAction(action: SelectLocationAction) {
        when (action) {
            is SelectLocationAction.OnQueryChange -> {
                changeQuery(query = action.query)
            }

            is SelectLocationAction.OnLocationClick -> {
                _uiState.update { it.copy(location = action.location) }
            }

            is SelectLocationAction.OnMapClick -> {
                _uiState.update {
                    it.copy(
                        location = LocationUiModel(
                            latitude = action.location.latitude,
                            longitude = action.location.longitude
                        )
                    )
                }
            }

            is SelectLocationAction.OnLocationSelect -> {
                _event.trySend(SelectLocationEvent.SelectLocation(action.location))
            }

            SelectLocationAction.OnBackClick -> {
                _event.trySend(SelectLocationEvent.NavigateBack)
            }
        }
    }

    private fun changeQuery(query: String) {
        _uiState.update { it.copy(query = query) }
    }
}