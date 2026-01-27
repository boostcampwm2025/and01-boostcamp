package com.andone.memorip.presentation.screen.placelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.domain.repository.TagRepository
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.placelist.model.PlaceListAction
import com.andone.memorip.presentation.screen.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.screen.placelist.model.PlaceListUiState
import com.andone.memorip.presentation.screen.placelist.model.RegionUiModel
import com.andone.memorip.presentation.screen.placelist.model.SelectedRegionState
import com.andone.memorip.presentation.screen.placelist.model.toUiModel
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
class PlaceListViewModel @Inject constructor(
    repository: PlaceRepository,
    tagRepository: TagRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(value = PlaceListUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PlaceListEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        val rootRegions = repository.loadRegions().map { it.toUiModel() }

        _uiState.update {
            it.copy(rootRegions = rootRegions)
        }
    }

    val placesPagingFlow =
        repository.getPlaceList()
            .map { pagingData ->
                pagingData.map { it.toUiModel() }
            }
            .cachedIn(viewModelScope)

    val tagsPagingFlow =
        tagRepository.getTagList()
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
                _event.trySend(element = PlaceListEvent.NavigateToPlaceDetail(id = action.id))
            }

            is PlaceListAction.OnQueryChange -> {
                _uiState.update { it.copy(query = action.query) }
            }

            PlaceListAction.OnRefreshPull -> {
                _event.trySend(element = PlaceListEvent.RefreshPagingData)
            }

            is PlaceListAction.OnRegionChipClick -> {
                onRegionClicked(region = action.region)
            }

            is PlaceListAction.OnTagChipClick -> {
                _uiState.update { it.copy(selectedTags = it.selectedTags + action.tag) }
            }

            is PlaceListAction.OnDeleteTagClick -> {
                _uiState.update { it.copy(selectedTags = it.selectedTags - action.tag) }
            }
        }
    }

    private fun onRegionClicked(region: RegionUiModel) {
        _uiState.update { state ->
            val parents = state.selectedRegionState.parents
            val child = state.selectedRegionState.child

            val nextState = when {
                region.level < state.currentLevel -> {
                    SelectedRegionState(
                        parents = parents.take(n = region.level) + region,
                        child = emptySet()
                    )
                }

                region.level == state.currentLevel -> {
                    val isChildEmpty = child.isEmpty()
                    val isAlreadySelected = parents.any { it.id == region.id } ||
                            child.any { it.id == region.id }

                    val nextChild =
                        if (isAlreadySelected) {
                            child.filterNot { it.id == region.id }.toSet()
                        } else {
                            if (isChildEmpty) {
                                setOf(parents.last()) + region
                            } else {
                                child + region
                            }
                        }

                    val nextParents =
                        if (isAlreadySelected) {
                            parents.filterNot { it.level == region.level }
                        } else {
                            if (isChildEmpty) {
                                parents.filterNot { it.level == region.level }
                            } else {
                                parents
                            }
                        }

                    state.selectedRegionState.copy(
                        parents = nextParents,
                        child = nextChild
                    )
                }

                else -> {
                    SelectedRegionState(
                        parents = parents + region,
                        child = emptySet()
                    )
                }
            }

            state.copy(selectedRegionState = nextState)
        }
    }
}