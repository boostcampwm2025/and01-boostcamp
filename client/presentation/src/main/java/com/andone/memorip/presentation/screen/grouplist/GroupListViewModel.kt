package com.andone.memorip.presentation.screen.grouplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.repository.GroupRepository
import com.andone.memorip.presentation.screen.grouplist.model.GroupListAction
import com.andone.memorip.presentation.screen.grouplist.model.GroupListEvent
import com.andone.memorip.presentation.screen.grouplist.model.GroupListUiState
import com.andone.memorip.presentation.screen.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val snackBarManager: SnackBarManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroupListUiState())
    val uiState: StateFlow<GroupListUiState> = _uiState
        .onStart {
            observeGroups()
            fetchInitialGroups()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GroupListUiState(isLoading = true)
        )

    private val _event = Channel<GroupListEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    private fun observeGroups() {
        viewModelScope.launch {
            groupRepository.myGroups.collect { groups ->
                _uiState.update { current ->
                    current.copy(
                        groups = groups.map { GroupUiModel.from(it) }.toImmutableList()
                    )
                }
            }
        }
    }

    private fun fetchInitialGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            groupRepository.fetchMyGroups()
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    snackBarManager.show(SnackBarEvent.NETWORK_ERROR)
                }
        }
    }

    fun onAction(action: GroupListAction) {
        when (action) {
            GroupListAction.OnFABClick -> {
                _event.trySend(GroupListEvent.NavigateToPlaceCreate)
            }

            is GroupListAction.OnGroupClick -> {
                _event.trySend(GroupListEvent.NavigateToGroupDetail(groupId = action.groupId))
            }
        }
    }
}