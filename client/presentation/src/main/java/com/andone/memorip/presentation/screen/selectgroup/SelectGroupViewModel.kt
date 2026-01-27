package com.andone.memorip.presentation.screen.selectgroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.model.Visibility
import com.andone.memorip.domain.repository.GroupRepository
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnAddGroupClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnBackClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnCheckClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnDialogCancelClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnDialogConfirmClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnFABClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupAction.OnGroupClick
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupEvent
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupUiModel
import com.andone.memorip.presentation.screen.selectgroup.model.SelectGroupUiState
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository,
    private val placeRepository: PlaceRepository,
    private val snackBarManager: SnackBarManager
) : ViewModel() {

    private var placeId: String? = null
    private var initialSelectedGroupId: String? = null

    private val _uiState = MutableStateFlow(SelectGroupUiState())
    val uiState: StateFlow<SelectGroupUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SelectGroupUiState(isLoading = true)
        )

    private val _event = Channel<SelectGroupEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    fun setPlaceId(placeId: String?) {
        // placeId가 변경되었거나, 같은 placeId로 다시 진입한 경우에도 초기화
        // (이전 선택 상태를 유지하지 않고 항상 API에서 최신 데이터를 가져옴)
        this.placeId = placeId
        // placeId가 있으면 initialSelectedGroupId 초기화 (PlaceDetailScreen용)
        if (placeId != null) {
            this.initialSelectedGroupId = null
        }
        resetAndFetchGroups()
    }

    fun setInitialSelectedGroupId(groupId: String?) {
        // PlaceCreate 화면에서 선택된 그룹 ID를 설정
        // placeId가 null일 때만 사용됨
        this.initialSelectedGroupId = groupId
    }

    /**
     * 상태를 초기화하고 API에서 최신 데이터를 조회
     * 화면이 나타날 때마다 호출되어 이전 선택 상태를 초기화함
     */
    fun resetAndFetchGroups() {
        // 상태 초기화
        _uiState.update {
            SelectGroupUiState(
                groups = emptyList<SelectGroupUiModel>().toImmutableList(),
                selectedGroupIds = persistentSetOf(),
                initialSelectedGroupIds = persistentSetOf(),
                isLoading = true
            )
        }
        fetchInitialGroups()
    }

    fun onAction(action: SelectGroupAction) {
        when (action) {
            is SelectGroupAction.OnInitialize -> {
                setPlaceId(action.placeId)
                if (action.placeId == null) {
                    setInitialSelectedGroupId(action.initialSelectedGroupId)
                }
            }

            OnFABClick -> {
                _event.trySend(element = SelectGroupEvent.ShowDialog)
            }

            is OnGroupClick -> {
                toggleGroupSelection(action.group.id)
            }

            OnAddGroupClick -> {
                _event.trySend(element = SelectGroupEvent.ShowDialog)
            }

            OnBackClick -> {
                _event.trySend(element = SelectGroupEvent.NavigateBack)
            }

            OnCheckClick -> {
                if (placeId != null) {
                    // PlaceDetailScreen: addGroupIds와 removeGroupIds 둘 다 계산
                    confirmGroupSelection()
                } else {
                    // PlaceCreateContainer: addGroupIds만 전송 (초기 선택이 없으므로)
                    val currentState = _uiState.value
                    val selectedGroupIds = currentState.selectedGroupIds.toList()

                    if (selectedGroupIds.isNotEmpty()) {
                        viewModelScope.launch {
                            // PlaceCreateContainer에서는 placeId가 없으므로 실제 API 호출은 하지 않고
                            // 선택된 그룹만 콜백으로 전달
                            val selectedGroup = currentState.groups.firstOrNull {
                                it.id in currentState.selectedGroupIds
                            }
                            if (selectedGroup != null) {
                                _event.trySend(element = SelectGroupEvent.SelectGroup(group = selectedGroup))
                            }
                        }
                    }
                }
            }

            is OnDialogConfirmClick -> {
                addGroup(groupName = action.groupName)
            }

            OnDialogCancelClick -> {
                _event.trySend(element = SelectGroupEvent.DismissDialog)
            }
        }
    }

    private fun toggleGroupSelection(groupId: String) {
        _uiState.update { current ->
            val newSelectedIds = if (groupId in current.selectedGroupIds) {
                current.selectedGroupIds.filter { it != groupId }.toImmutableSet()
            } else {
                (current.selectedGroupIds + groupId).toImmutableSet()
            }
            current.copy(selectedGroupIds = newSelectedIds)
        }
    }

    private fun confirmGroupSelection() {
        if (placeId == null) return

        val currentState = _uiState.value
        val initialSelectedIds = currentState.initialSelectedGroupIds
        val currentSelectedIds = currentState.selectedGroupIds

        val addGroupIds = (currentSelectedIds - initialSelectedIds).toList()
        val removeGroupIds = (initialSelectedIds - currentSelectedIds).toList()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            placeRepository.updatePlaceGroups(
                placeId = placeId!!,
                addGroupIds = addGroupIds,
                removeGroupIds = removeGroupIds
            )
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            initialSelectedGroupIds = currentSelectedIds
                        )
                    }
                    snackBarManager.show(SnackBarEvent.SUCCESS)
                    _event.trySend(SelectGroupEvent.PlaceGroupsUpdated)
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                    snackBarManager.show(SnackBarEvent.NETWORK_ERROR)
                }
        }
    }

    private fun fetchInitialGroups() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (placeId != null) {
                // PlaceDetail 화면: API에서 최신 데이터를 조회하여 초기 상태 설정
                // 항상 API에서 받아온 값으로 initialSelectedGroupIds를 설정
                groupRepository.fetchMyGroupsWithPlaceStatus(placeId = placeId)
                    .onSuccess { groupsWithPlaceAdded ->
                        val groups = groupsWithPlaceAdded.map { SelectGroupUiModel.from(it) }
                            .toImmutableList()
                        // API에서 받아온 isPlaceAdded = true인 그룹들을 초기 선택 상태로 설정
                        val initiallySelectedIds = groups
                            .filter { it.isPlaceAdded }
                            .map { it.id }
                            .toImmutableSet()

                        _uiState.update { current ->
                            current.copy(
                                groups = groups,
                                selectedGroupIds = initiallySelectedIds,
                                // API에서 조회한 값으로 initialSelectedGroupIds 설정
                                // 이후 사용자가 변경한 내용과 비교하기 위해 사용
                                initialSelectedGroupIds = initiallySelectedIds,
                                isLoading = false
                            )
                        }
                    }
                    .onFailure {
                        _uiState.update { it.copy(isLoading = false) }
                    }
            } else {
                groupRepository.fetchMyGroups()
                    .onSuccess {
                        val groups = groupRepository.myGroups.first()
                        val initiallySelectedIds = if (initialSelectedGroupId != null) {
                            persistentSetOf(initialSelectedGroupId!!)
                        } else {
                            persistentSetOf()
                        }
                        _uiState.update { current ->
                            current.copy(
                                groups = groups.map { SelectGroupUiModel.from(it) }
                                    .toImmutableList(),
                                selectedGroupIds = initiallySelectedIds,
                                initialSelectedGroupIds = initiallySelectedIds,
                                isLoading = false
                            )
                        }
                    }
                    .onFailure {
                        _uiState.update { it.copy(isLoading = false) }
                    }
            }
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
