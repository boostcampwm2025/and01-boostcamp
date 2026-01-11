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

    private val regionTree = repository.loadRegions().map { it.toUiModel() }
    private var currentLevel = 0

    private val _uiState = MutableStateFlow(
        value = PlaceListUiState(
            currentRegionList = regionTree.first().child
        )
    )
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

    private fun updateSelectedState(region: RegionUiModel) {
        val selectedChild = _uiState.value.selectedRegionState.child

        when {

            region.level == currentLevel -> {
                if (selectedChild.isNotEmpty()) {
                    _uiState.update {
                        it.copy(
                            selectedRegionState = it.selectedRegionState.copy(
                                child = it.selectedRegionState.child + region
                            )
                        )
                    }
                } else {
                    val last = _uiState.value.selectedRegionState.parents.last()
                    val rest = _uiState.value.selectedRegionState.parents.dropLast(n = 1)
                    _uiState.update {
                        it.copy(
                            selectedRegionState = it.selectedRegionState.copy(
                                parents = rest,
                                child = listOf(last)
                            )
                        )
                    }
                }
            }

            region.level > currentLevel -> {
                _uiState.update {
                    it.copy(
                        currentRegionList = region.child,
                        selectedRegionState = it.selectedRegionState.copy(
                            parents = it.selectedRegionState.parents + region
                        )
                    )
                }
                currentLevel++
            }

            else -> {
                val diff = currentLevel - region.level
                val rest = _uiState.value.selectedRegionState.parents.dropLast(n = diff - 1)
                _uiState.update {
                    it.copy(
                        selectedRegionState = it.selectedRegionState.copy(
                            parents = rest,
                            child = emptyList()
                        )
                    )
                }
            }

        }

    }

    private fun updateLevel(region: RegionUiModel) {
        var current = _uiState.value.currentRegionList.first()

        when {
            region.level > currentLevel -> {
                while (current.level + 1 < region.level) {
                    current = current.child.first()
                }
                _uiState.update {
                    it.copy(
                        currentRegionList = current.child
                    )
                }
                currentLevel = current.level + 1
            }

            region.level < currentLevel -> {
                while (current.level + 1 > region.level){
                    if (current.parent == null) {
                        _uiState.update { it.copy(
                            currentRegionList = regionTree.first().child
                        ) }
                        currentLevel = 0
                    } else{
                        current = current.parent
                    }
                }
                _uiState.update { it.copy(
                    currentRegionList = current.child
                ) }
                currentLevel = current.level + 1
            }

            else -> {}
        }
    }
}