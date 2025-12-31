package com.andone.memorip.presentation.placelist

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.placelist.model.PlaceListEvent
import com.andone.memorip.presentation.placelist.model.PlaceListUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class PlaceListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(value = PlaceListUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PlaceListEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()
}