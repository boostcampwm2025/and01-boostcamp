package com.andone.memorip.presentation.placelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.andone.memorip.domain.model.Region
import com.andone.memorip.domain.repository.PlaceListRepository
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.placelist.model.PlaceListAction
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.placelist.model.PlaceListUiState
import com.andone.memorip.presentation.placelist.model.RegionChipModel
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

    private val _uiState = MutableStateFlow(value = PlaceListUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PlaceListEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    private lateinit var rootRegions: List<Region>
    private var regionPath: List<Region> = emptyList()

    init {
        rootRegions = repository.loadRegions()
        regionPath = emptyList()

        _uiState.update {
            it.copy(currentRegionList = rootRegions.toChipModels(level = 0))
        }
    }

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

    private fun List<Region>.toChipModels(level: Int): List<RegionChipModel> =
        map { region ->
            RegionChipModel(
                name = region.name,
                level = level
            )
        }

    private fun currentRegions(): List<Region> = regionPath.lastOrNull()?.subRegions ?: rootRegions

    private fun onRegionSelected(region: Region) {
        regionPath = regionPath + region
        val level = regionPath.size

        _uiState.update {
            it.copy(currentRegionList = region.subRegions.toChipModels(level))
        }
    }

    private fun onRegionBack() {
        if (regionPath.isEmpty()) return

        regionPath = regionPath.dropLast(1)
        val level = regionPath.size
        val regions = currentRegions()

        _uiState.update {
            it.copy(currentRegionList = regions.toChipModels(level))
        }
    }
}