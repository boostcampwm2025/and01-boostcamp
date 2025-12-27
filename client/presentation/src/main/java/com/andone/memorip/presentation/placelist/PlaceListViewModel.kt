package com.andone.memorip.presentation.placelist

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.placelist.model.PlaceListAction
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.placelist.model.PlaceListUiState
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
class PlaceListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceListUiState())
    val uiState: StateFlow<PlaceListUiState> = _uiState

    private val _event = Channel<PlaceListEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        // 테스트 더미 데이터
        _uiState.update { it.copy(groups = DummyData.groups) }
    }

    fun onAction(action: PlaceListAction) {
        when (action) {
            PlaceListAction.OnFABClick -> {
                _event.trySend(PlaceListEvent.NavigateToPlaceCreate)
            }

            is PlaceListAction.OnGroupClick -> {
                _event.trySend(PlaceListEvent.NavigateToGroupDetail(action.groupId))
            }
        }
    }
}