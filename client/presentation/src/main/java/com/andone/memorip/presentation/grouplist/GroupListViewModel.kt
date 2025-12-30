package com.andone.memorip.presentation.grouplist

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.grouplist.model.GroupListAction
import com.andone.memorip.presentation.grouplist.model.GroupListEvent
import com.andone.memorip.presentation.grouplist.model.GroupListUiState
import com.andone.memorip.presentation.util.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GroupListUiState())
    val uiState: StateFlow<GroupListUiState> = _uiState

    private val _event = Channel<GroupListEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        // 테스트 더미 데이터
        _uiState.update { it.copy(groups = DummyData.groups) }
    }

    fun onAction(action: GroupListAction) {
        when (action) {
            GroupListAction.OnFABClick -> {
                _event.trySend(GroupListEvent.NavigateToPlaceCreate)
            }

            is GroupListAction.OnGroupClick -> {
                _event.trySend(GroupListEvent.NavigateToGroupDetail(action.groupId))
            }
        }
    }
}