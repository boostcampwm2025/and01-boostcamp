package com.andone.memorip.presentation.screen.groupdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map as pagingMap
import com.andone.memorip.domain.repository.GroupRepository
import com.andone.memorip.navigation.GroupDetail
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailAction
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailEvent
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailUiState
import com.andone.memorip.presentation.screen.groupdetail.model.MapBottomSheetStep
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = GroupDetailViewModel.Factory::class)
class GroupDetailViewModel @AssistedInject constructor(
    @Assisted route: GroupDetail,
    private val repository: GroupRepository
) : ViewModel() {

    private val groupId: String = route.groupId

    private val _uiState = MutableStateFlow(GroupDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<GroupDetailEvent>(BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getGroupById(groupId).onSuccess { group ->
                _uiState.update { it.copy(groupName = group.title) }
            }
        }
    }

    val placesPagingFlow: Flow<PagingData<Place>> =
        repository.getGroupPlaces(groupId)
            .map { pagingData ->
                pagingData.pagingMap { it.toUiModel() }
            }
            .cachedIn(viewModelScope)

    fun onAction(action: GroupDetailAction) {
        when (action) {
            GroupDetailAction.OnMenuClick -> {
                _uiState.update { it.copy(expanded = true) }
            }

            GroupDetailAction.OnBackClick -> {
                _event.trySend(element = GroupDetailEvent.NavigateBack)
            }

            is GroupDetailAction.OnPlaceClick -> {
                _event.trySend(element = GroupDetailEvent.NavigateToPlaceDetail(id = action.id))
            }

            GroupDetailAction.OnSearchClick -> {
                /** Search 버튼 눌렀을 때의 event 처리 */
            }

            is GroupDetailAction.OnTabClick -> {
                _uiState.update { it.copy(currentTab = action.currentTab) }
            }

            is GroupDetailAction.OnMapPlaceClick -> {
                _uiState.update {
                    it.copy(
                        mapSelectedPlace = action.place,
                        mapBottomSheetContent = MapBottomSheetStep.PlaceDetail
                    )
                }
            }

            GroupDetailAction.OnMapPlaceClose -> {
                _uiState.update {
                    it.copy(
                        mapSelectedPlace = null,
                        mapBottomSheetContent = MapBottomSheetStep.PlaceList
                    )
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: GroupDetail): GroupDetailViewModel
    }
}