package com.andone.memorip.presentation.selectgroup

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.placelist.model.GroupUiModel
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.onAddGroupClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.onBackClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.onCancelDialogClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.onConfirmDialogClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.onFABClick
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.onGroupClick
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

    private val _uiState: MutableStateFlow<SelectGroupUiState> =
        MutableStateFlow(SelectGroupUiState())
    val uiState = _uiState.asStateFlow()

    private val _event: Channel<SelectGroupEvent> = Channel(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        _uiState.value = SelectGroupUiState(groups = groups.toImmutableList())
    }

    fun onAction(action: SelectGroupAction) {
        when (action) {
            onFABClick -> _event.trySend(element = SelectGroupEvent.onShowDialog)
            is onGroupClick -> _event.trySend(element = SelectGroupEvent.onNavigateAddPlace(group = action.group))
            onAddGroupClick -> _event.trySend(element = SelectGroupEvent.onShowDialog)
            onBackClick -> _event.trySend(element = SelectGroupEvent.onNavigateBack)
            is onConfirmDialogClick -> onAddGroup(newGroup = action.newGroup)
            onCancelDialogClick -> _event.trySend(element = SelectGroupEvent.onDismissDialog)
        }
    }

    private fun onAddGroup(newGroup: GroupUiModel) {
        val newGroups = uiState.value.groups + newGroup
        _uiState.value = uiState.value.copy(groups = newGroups.toImmutableList())
        _event.trySend(element = SelectGroupEvent.onDismissDialog)
    }
}