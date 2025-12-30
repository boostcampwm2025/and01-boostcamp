package com.andone.memorip.presentation.placedetail


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.navigation.PlaceDetail
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
    route: PlaceDetail
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PlaceDetailEvent>(BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        loadPlaceDetail(route.placeId)
    }

    fun onAction(action: PlaceDetailAction) {
        when (action) {
            PlaceDetailAction.OnBackClick -> _event.trySend(PlaceDetailEvent.NavigateBack)
        }
    }

    fun loadPlaceDetail(id: Int) {
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