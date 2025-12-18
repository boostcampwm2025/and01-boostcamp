package com.andone.memorip.presentation.home

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.home.model.GroupUiModel
import com.andone.memorip.presentation.home.model.HomeAction
import com.andone.memorip.presentation.home.model.HomeUiState
import com.andone.memorip.presentation.util.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        // 테스트 더미 데이터
        _uiState.update { it.copy(groups = DummyData.groups) }
    }

    fun onAction(intent: HomeAction) {
        when (intent) {
            is HomeAction.OnGroupAdd -> {
                reduceAddGroup(name = intent.name)
            }

            is HomeAction.OnGroupUpdate -> {
                reduceUpdateGroup(group = intent.group)
            }

            is HomeAction.OnGroupsSet -> {
                _uiState.value = HomeUiState(groups = intent.groups)
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