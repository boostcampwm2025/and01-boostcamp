package com.andone.memorip.presentation.placedetail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.repository.PlaceRepository
import com.andone.memorip.navigation.PlaceDetail
import com.andone.memorip.presentation.placedetail.model.PlaceDetailAction
import com.andone.memorip.presentation.placedetail.model.PlaceDetailEvent
import com.andone.memorip.presentation.placedetail.model.PlaceDetailUiState
import com.andone.memorip.presentation.placedetail.model.toUiModel
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

@HiltViewModel(assistedFactory = PlaceDetailViewModel.Factory::class)
class PlaceDetailViewModel @AssistedInject constructor(
    @Assisted route: PlaceDetail,
    private val placeRepository: PlaceRepository,
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
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: PlaceDetail): PlaceDetailViewModel
    }
}