package com.andone.memorip.presentation.selectgroup

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.home.model.GroupUiModel
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction
import com.andone.memorip.presentation.selectgroup.model.SelectGroupAction.*
import com.andone.memorip.presentation.selectgroup.model.SelectGroupEvent
import com.andone.memorip.presentation.selectgroup.model.SelectGroupUiState
import com.andone.memorip.presentation.util.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class SelectGroupViewModel @Inject constructor(): ViewModel() {

    private val _uiState: MutableStateFlow<SelectGroupUiState> = MutableStateFlow(SelectGroupUiState())
    val uiState = _uiState.asStateFlow()

    private val _event: Channel<SelectGroupEvent> = Channel(BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        _uiState.value = SelectGroupUiState(groups = DummyData.groups.toImmutableList())
    }

    fun onAction(action: SelectGroupAction) {
        when(action) {
            onFABClick -> _event.trySend(SelectGroupEvent.onShowDialog)
            is onGroupClick -> _event.trySend(SelectGroupEvent.onNavigateAddPlace(action.group))
            onAddGroupClick -> _event.trySend(SelectGroupEvent.onShowDialog)
            onBackClick -> _event.trySend(SelectGroupEvent.onNavigateBack)
            is onConfirmDialogClick -> onAddGroup(action.newGroup)
            onDismissDialogClick -> _event.trySend(SelectGroupEvent.onDismissDialog)
        }
    }

    private fun onAddGroup(newGroup: GroupUiModel) {
        val newGroups = uiState.value.groups + newGroup
        _uiState.value = uiState.value.copy(groups = newGroups.toImmutableList())
        _event.trySend(SelectGroupEvent.onDismissDialog)
    }
}