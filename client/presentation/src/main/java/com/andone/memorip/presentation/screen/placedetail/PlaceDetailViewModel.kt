package com.andone.memorip.presentation.screen.placedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.repository.GroupRepository
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.navigation.PlaceDetail
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailAction
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailEvent
import com.andone.memorip.presentation.screen.placedetail.model.PlaceDetailUiState
import com.andone.memorip.presentation.screen.placedetail.model.toUiModel
import com.andone.memorip.presentation.util.snackbar.SnackBarEvent
import com.andone.memorip.presentation.util.snackbar.SnackBarManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = PlaceDetailViewModel.Factory::class)
class PlaceDetailViewModel @AssistedInject constructor(
    @Assisted route: PlaceDetail,
    private val placeRepository: PlaceRepository,
    private val groupRepository: GroupRepository,
    private val snackBarManager: SnackBarManager
) : ViewModel() {

    private val placeId = route.placeId

    val uiState = flow {
        placeRepository.getPlaceDetail(placeId = placeId)
            .onSuccess {
                emit(
                    value = PlaceDetailUiState(
                        place = it.toUiModel(),
                        isLoading = false
                    )
                )
            }
            .onFailure {
                emit(value = PlaceDetailUiState(isLoading = false))
                snackBarManager.show(event = SnackBarEvent.NETWORK_ERROR)
                _event.trySend(PlaceDetailEvent.NavigateBack)
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlaceDetailUiState(isLoading = true)
    )

    private val _event = Channel<PlaceDetailEvent>(BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: PlaceDetailAction) {
        when (action) {
            PlaceDetailAction.OnBackClick -> _event.trySend(PlaceDetailEvent.NavigateBack)
            PlaceDetailAction.OnAddToGroupClick -> _event.trySend(PlaceDetailEvent.NavigateToSelectGroup)
            PlaceDetailAction.GroupClick -> _event.trySend(PlaceDetailEvent.NavigateToGroupList)
            PlaceDetailAction.OnMoreClick -> {
                _event.trySend(PlaceDetailEvent.ShowMoreMenu)
            }

            PlaceDetailAction.OnMoreMenuDismiss -> {
                _event.trySend(PlaceDetailEvent.HideMoreMenu)
            }

            PlaceDetailAction.OnEditClick -> {
            }

            PlaceDetailAction.OnDeleteClick -> {
                _event.trySend(PlaceDetailEvent.ShowDeleteDialog)
            }

            PlaceDetailAction.OnDeleteDismiss -> {
                _event.trySend(PlaceDetailEvent.HideDeleteDialog)
            }

            PlaceDetailAction.OnDeleteConfirm -> {
                _event.trySend(PlaceDetailEvent.HideDeleteDialog)
                deletePlace()
            }
        }
    }

    fun addPlaceToGroup(groupId: String) {
        viewModelScope.launch {
            groupRepository.addPlaceToGroup(groupId, placeId)
                .onSuccess {
                    snackBarManager.show(event = SnackBarEvent.SUCCESS)
                    _event.trySend(PlaceDetailEvent.PlaceAddToGroup)
                }
                .onFailure {
                    snackBarManager.show(event = SnackBarEvent.NETWORK_ERROR)
                }
        }
    }

    private fun deletePlace() {
        viewModelScope.launch {
            placeRepository.deletePlace(placeId)
                .onSuccess {
                    snackBarManager.show(event = SnackBarEvent.SUCCESS)
                    _event.trySend(PlaceDetailEvent.NavigateBack)
                }
                .onFailure {
                    snackBarManager.show(event = SnackBarEvent.UNKNOWN_ERROR)
                }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: PlaceDetail): PlaceDetailViewModel
    }
}
