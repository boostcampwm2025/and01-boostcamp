package com.andone.memorip.presentation.placedetail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.navigation.PlaceDetailRoute
import com.andone.memorip.presentation.placedetail.model.PlaceDetailAction
import com.andone.memorip.presentation.placedetail.model.PlaceDetailEvent
import com.andone.memorip.presentation.placedetail.model.PlaceDetailUiState
import com.andone.memorip.presentation.util.DummyData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaceDetailViewModel(
    route: PlaceDetailRoute
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<PlaceDetailEvent>(BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadPlaceDetail(route.id)
    }

    fun onAction(action: PlaceDetailAction) {
        when (action) {
            PlaceDetailAction.OnNavigateBack -> _uiEvent.trySend(PlaceDetailEvent.NavigateBack)
        }
    }

    fun loadPlaceDetail(id: Long) {
        // 장소 상세 불러오기 api 추가될 예정
        // 우선 더미데이터 사용
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update {
                it.copy(
                    place = DummyData.place,
                    isLoading = false
                )
            }
        }
    }
}