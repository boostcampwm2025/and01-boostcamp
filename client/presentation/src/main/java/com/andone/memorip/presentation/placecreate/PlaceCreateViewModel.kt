package com.andone.memorip.presentation.placecreate

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.model.Category
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.placecreate.model.PlaceCreateAction
import com.andone.memorip.presentation.placecreate.model.PlaceCreateEvent
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
                _event.trySend(PlaceCreateEvent.NavigateToCategory)
            }

            PlaceCreateAction.OnLocationClick -> {
                _event.trySend(PlaceCreateEvent.NavigateToLocation)
            }

            PlaceCreateAction.OnGroupClick -> {
                _event.trySend(PlaceCreateEvent.NavigateToGroup)
            }

            is PlaceCreateAction.OnTitleChange -> {
                _uiState.update { it.copy(title = action.title) }
            }

            is PlaceCreateAction.OnContentChange -> {
                _uiState.update { it.copy(content = action.content) }
            }

            PlaceCreateAction.OnPublicChange -> {
                _uiState.update { it.copy(isPublic = !_uiState.value.isPublic) }
            }

            is PlaceCreateAction.OnImagesAdd -> {
                _uiState.update { it.copy(images = it.images + action.images) }
            }

            is PlaceCreateAction.OnImagesRemove -> {
                _uiState.update { it.copy(images = it.images - action.imageUri) }
            }

            PlaceCreateAction.OnPlaceCreate -> {
                createPlace()
            }

            PlaceCreateAction.OnSnackBarShow -> {
                _event.trySend(PlaceCreateEvent.ShowSnackBar)
            }

            PlaceCreateAction.OnBackClick -> {
                _event.trySend(PlaceCreateEvent.NavigateBack)
            }
        }
    }

    fun updateCategory(category: List<Category>) {
        _uiState.update { it.copy(category = category) }
    }

    fun updateLocation(location: LocationUiModel) {
        _uiState.update { it.copy(location = location) }
    }

    fun updateGroup(group: GroupUiModel) {
        _uiState.update { it.copy(group = group) }
    }

    fun createPlace() {

    }
}