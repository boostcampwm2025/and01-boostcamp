package com.andone.memorip.presentation.placelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.andone.memorip.domain.repository.PlaceListRepository
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.placelist.model.PlaceListAction
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.placelist.model.PlaceListUiState
import com.andone.memorip.presentation.placelist.model.RegionUiModel
import com.andone.memorip.presentation.placelist.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PlaceListViewModel @Inject constructor(repository: PlaceListRepository) : ViewModel() {

    private val rootRegions = repository.loadRegions().map { it.toUiModel() }

    private val _uiState = MutableStateFlow(value = PlaceListUiState(rootRegions = rootRegions))
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PlaceListEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    val placesPagingFlow =
        repository.getPlaceList()
            .map { pagingData ->
                pagingData.map { it.toUiModel() }
            }
            .cachedIn(viewModelScope)

    fun onAction(action: PlaceListAction) {
        when (action) {
            PlaceListAction.OnFABClick -> {
                _event.trySend(element = PlaceListEvent.NavigateToPlaceCreate)
            }

            is PlaceListAction.OnPlaceClick -> {
                _event.trySend(element = PlaceListEvent.NavigatePlaceDetail(id = action.id))
            }

            is PlaceListAction.OnQueryChange -> {
                _uiState.update { it.copy(query = action.query) }
            }
        }
    }

    private fun onRegionClicked(region: RegionUiModel) {
        _uiState.update { state ->
            val parents = state.selectedRegionState.parents

            val nextParents = when {
                region.level < parents.size ->
                    parents.take(region.level) + region

                region.level == parents.size ->
                    parents.dropLast(1) + region

                else ->
                    parents + region
            }

            state.copy(
                selectedRegionState = state.selectedRegionState.copy(
                    parents = nextParents,
                    child = emptyList()
                )
            )
        }
    }
}