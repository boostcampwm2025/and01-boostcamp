package com.andone.memorip.presentation.screen.selectgroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.GroupRepository
import com.andone.memorip.presentation.screen.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnAddGroupClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnBackClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnDialogCancelClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnDialogConfirmClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnFABClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnGroupClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupEvent
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupUiState
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
class SelectGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectGroupUiState())
    val uiState: StateFlow<SelectGroupUiState> = _uiState
        .onStart {
            observeGroups()
            fetchInitialGroups()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SelectGroupUiState(isLoading = true)
        )

    private val _event = Channel<SelectGroupEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: SelectGroupAction) {
        when (action) {
            OnFABClick -> {
                _event.trySend(element = SelectGroupEvent.ShowDialog)
            }

            is OnGroupClick -> {
                _event.trySend(element = SelectGroupEvent.SelectGroup(group = action.group))
            }

            OnAddGroupClick -> {
                _event.trySend(element = SelectGroupEvent.ShowDialog)
            }

            OnBackClick -> {
                _event.trySend(element = SelectGroupEvent.NavigateBack)
            }

            is OnDialogConfirmClick -> {
                addGroup(groupName = action.groupName)
            }

            OnDialogCancelClick -> {
                _event.trySend(element = SelectGroupEvent.DismissDialog)
            }
        }
    }

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
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun addGroup(groupName: String) {
        viewModelScope.launch {
            // TODO: 실제 로그인한 사용자의 ID로 교체해야 합니다. 지금은 백엔드 자동 삽입 방식임.
            val ownerId = "current_user_id"

            _uiState.update { it.copy(isLoading = true) }

            // todo: visibility 받는 걸로 수정
            groupRepository.createGroup(
                ownerId = ownerId,
                title = groupName,
                visibility = Visibility.PUBLIC
            ).onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                _event.trySend(SelectGroupEvent.DismissDialog)
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
                _event.trySend(SelectGroupEvent.ShowSnackBar)
            }
        }
    }
}