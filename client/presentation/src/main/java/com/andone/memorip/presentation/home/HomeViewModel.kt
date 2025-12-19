package com.andone.memorip.presentation.home

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.home.model.GroupUiModel
import com.andone.memorip.presentation.home.model.HomeIntent
import com.andone.memorip.presentation.home.model.HomeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.AddGroup -> { reduceAddGroup(name = intent.name) }
            is HomeIntent.UpdateGroup -> { reduceUpdateGroup(group = intent.group) }
            is HomeIntent.SetGroups -> { _state.value = HomeState(groups = intent.groups) }
        }
    }

    private fun reduceAddGroup(name: String) {
        _state.update { current ->
            current.copy(groups = current.groups + GroupUiModel.create(name))
        }
    }

    private fun reduceUpdateGroup(group: GroupUiModel) {
        _state.update { current ->
            current.copy(
                groups = current.groups.map {
                    if (it.id == group.id) group else it
                }
            )
        }
    }
}