package com.andone.memorip.presentation.selectlocation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.repository.NaverSearchRepository
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.selectlocation.SelectLocationViewModelConstants.TIMEOUT_MILLS
import com.andone.memorip.presentation.selectlocation.model.SelectLocationAction
import com.andone.memorip.presentation.selectlocation.model.SelectLocationEvent
import com.andone.memorip.presentation.selectlocation.model.SelectLocationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private object SelectLocationViewModelConstants {
    const val TIMEOUT_MILLS = 500L
}

@HiltViewModel
class SelectLocationViewModel @Inject constructor(
    private val searchRepository: NaverSearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectLocationUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SelectLocationEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        collectSearchQuery()
    }

    fun onAction(action: SelectLocationAction) {
        when (action) {
            is SelectLocationAction.OnQueryChange -> {
                changeQuery(action.query)
            }

            is SelectLocationAction.OnSelectLocationClick -> {
                _uiState.update { it.copy(location = action.location) }
            }

            SelectLocationAction.OnBackClick -> {
                _event.trySend(SelectLocationEvent.NavigateBack)
            }
        }
    }

    private fun changeQuery(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    @OptIn(FlowPreview::class)
    private fun collectSearchQuery() {
        viewModelScope.launch {
            _uiState
                .map { it.query }
                .distinctUntilChanged()
                .debounce(TIMEOUT_MILLS)
                .collectLatest { query ->
                    searchLocations(query = query)
                }
        }
    }

    private suspend fun searchLocations(query: String) {
        if (query.isBlank()) {
            _uiState.update { it.copy() }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        searchRepository.searchLocations(query = query)
            .onSuccess { result ->
                _uiState.update {
                    it.copy(
                        locations = result.map { dto -> dto.toUiModel() }.toImmutableList(),
                        isLoading = false
                    )
                }
                Log.d("searchLocations", result.toString())
            }.onFailure { exception ->
                Log.d("searchLocations", exception.message.toString())
                // 오류 상황 설정 필요
            }
    }
}