package com.andone.memorip.presentation.placecreate

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.placecreate.model.PlaceCreateEvent
import com.andone.memorip.presentation.placecreate.model.PlaceCreateScreenState
import com.andone.memorip.presentation.placecreate.model.PlaceCreateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PlaceCreateViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceCreateUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<PlaceCreateEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: PlaceCreateAction) {
        when (action) {
            PlaceCreateAction.OnCategoryClick -> {
                _uiState.update { it.copy(screenState = PlaceCreateScreenState.SELECT_CATEGORY) }
            }

            PlaceCreateAction.OnLocationClick -> {
                _uiState.update { it.copy(screenState = PlaceCreateScreenState.SELECT_LOCATION) }
            }

            PlaceCreateAction.OnGroupClick -> {
                _uiState.update { it.copy(screenState = PlaceCreateScreenState.SELECT_GROUP) }
            }

            is PlaceCreateAction.OnTitleChange -> {
                _uiState.update { it.copy(title = action.title) }
            }

            is PlaceCreateAction.OnContentChange -> {
                _uiState.update { it.copy(content = action.content) }
            }

            is PlaceCreateAction.OnAddImages -> {
                _uiState.update { it.copy(images = it.images + action.images) }
            }

            is PlaceCreateAction.OnRemoveImages -> {
                _uiState.update { it.copy(images = it.images - action.imageUri) }
            }

            PlaceCreateAction.OnBackClick -> {
                _event.trySend(PlaceCreateEvent.NavigateBack)
            }
        }
    }
}