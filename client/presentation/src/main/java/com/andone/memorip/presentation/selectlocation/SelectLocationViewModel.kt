package com.andone.memorip.presentation.selectlocation

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.selectlocation.model.SelectLocationAction
import com.andone.memorip.presentation.selectlocation.model.SelectLocationEvent
import com.andone.memorip.presentation.selectlocation.model.SelectLocationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SelectLocationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SelectLocationUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SelectLocationEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: SelectLocationAction) {
        when (action) {
            is SelectLocationAction.OnSelectLocationClick -> {
                _uiState.update { it.copy(location = action.location) }
            }

            SelectLocationAction.OnBackClick -> {
                _event.trySend(SelectLocationEvent.NavigateBack)
            }
        }
    }
}