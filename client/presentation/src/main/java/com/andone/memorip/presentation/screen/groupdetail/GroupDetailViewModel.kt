package com.andone.memorip.presentation.screen.groupdetail

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailAction
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailEvent
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailUiState
import com.andone.memorip.presentation.util.DummyData.groupName
import com.andone.memorip.presentation.util.DummyData.places
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class GroupDetailViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(GroupDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<GroupDetailEvent>(BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        _uiState.value = uiState.value.copy(
            groupName = groupName,
            places = places.toImmutableList()
        )
    }

    fun onAction(action: GroupDetailAction) {
        when (action) {
            GroupDetailAction.OnMenuClick -> {
                _uiState.value = _uiState.value.copy(expanded = true)
            }

            GroupDetailAction.OnBackClick -> {
                _event.trySend(element = GroupDetailEvent.NavigateBack)
            }

            is GroupDetailAction.OnPictureClick -> {
                _uiState.value = _uiState.value.copy(selectedPlace = action.place)
            }

            GroupDetailAction.OnDismissBottomSheetClick -> {
                _uiState.value = _uiState.value.copy(selectedPlace = null)
            }

            is GroupDetailAction.OnPlaceClick -> {
                _event.trySend(element = GroupDetailEvent.NavigatePlaceDetail(id = action.id))
            }

            GroupDetailAction.OnSearchClick -> {
                /** Search 버튼 눌렀을 때의 event 처리 */
            }

            is GroupDetailAction.OnTabClick -> {
                _uiState.value = _uiState.value.copy(currentTab = action.currentTab)
            }
        }
    }
}