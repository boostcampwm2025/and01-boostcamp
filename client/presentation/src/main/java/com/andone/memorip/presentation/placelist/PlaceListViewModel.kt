package com.andone.memorip.presentation.placelist

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.placelist.model.GroupUiModel
import com.andone.memorip.presentation.placelist.model.PlaceListAction
import com.andone.memorip.presentation.placelist.model.PlaceListUiState
import com.andone.memorip.presentation.util.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PlaceListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceListUiState())
    val uiState: StateFlow<PlaceListUiState> = _uiState

    init {
        // 테스트 더미 데이터
        _uiState.update { it.copy(groups = DummyData.groups) }
    }

    fun onAction(intent: PlaceListAction) {
        when (intent) {
            is PlaceListAction.OnGroupAdd -> {
                reduceAddGroup(name = intent.name)
            }

            is PlaceListAction.OnGroupUpdate -> {
                reduceUpdateGroup(group = intent.group)
            }

            is PlaceListAction.OnGroupsSet -> {
                _uiState.value = PlaceListUiState(groups = intent.groups)
            }
        }
    }

    private fun reduceAddGroup(name: String) {
        _uiState.update { current ->
            current.copy(groups = current.groups + GroupUiModel.create(name))
        }
    }

    private fun reduceUpdateGroup(group: GroupUiModel) {
        _uiState.update { current ->
            current.copy(
                groups = current.groups.map {
                    if (it.id == group.id) group else it
                }
            )
        }
    }
}