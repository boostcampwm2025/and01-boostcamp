package com.andone.memorip.presentation.selectgroup

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.OnAddGroupClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.OnBackClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.OnDialogCancelClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.OnDialogConfirmClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.OnFABClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.OnGroupClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupEvent
import com.andone.memorip.presentation.selectgroup.model.SelectGroupUiState
import com.andone.memorip.presentation.util.DummyData.groups
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SelectGroupViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SelectGroupUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SelectGroupEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        // 더미데이터
        _uiState.value = SelectGroupUiState(groups = groups.toImmutableList())
    }

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
                addGroup(newGroup = action.newGroup)
            }

            OnDialogCancelClick -> {
                _event.trySend(element = SelectGroupEvent.DismissDialog)
            }
        }
    }

    private fun addGroup(newGroup: GroupUiModel) {
        val newGroups = uiState.value.groups + newGroup
        _uiState.value = uiState.value.copy(groups = newGroups.toImmutableList())
        _event.trySend(element = SelectGroupEvent.DismissDialog)
    }
}