package com.andone.memorip.presentation.screen.selectcategory

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.andone.memorip.domain.repository.TagRepository
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.model.toDomainModel
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.selectcategory.Constants.MAX_SELECTABLE_COUNT
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryAction
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryError
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryEvent
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

private object Constants {
    const val MAX_SELECTABLE_COUNT = 3
}

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val tagRepository: TagRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SelectCategoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SelectCategoryEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    val tagsPagingFlow = tagRepository.loadTags("019b8be0-1fad-71e9-9da0-bc03ada63862")
        .map { pagingData ->
            pagingData.map { it.toUiModel() }
        }
        .cachedIn(viewModelScope)

    fun onAction(action: SelectCategoryAction) {
        when (action) {
            SelectCategoryAction.OnBackClick -> {
                _event.trySend(element = SelectCategoryEvent.NavigateBack)
            }

            is SelectCategoryAction.OnConfirmClick -> {
                _event.trySend(element = SelectCategoryEvent.SelectCategory(categories = _uiState.value.checkedCategories))
            }

            SelectCategoryAction.OnDialogCancelClick -> {
                _event.trySend(element = SelectCategoryEvent.DismissDialog)
            }

            is SelectCategoryAction.OnCategoryItemClick -> {
                updateChecked(tag = action.tagUiModel)
            }

            is SelectCategoryAction.OnDialogConfirmClick -> {
                addCategory(
                    category = action.category,
                    color = action.color
                )
            }

            SelectCategoryAction.OnFABClick -> {
                _event.trySend(element = SelectCategoryEvent.ShowDialog)
            }
        }
    }

    private fun addCategory(category: String, color: Color) {
        val newTag = TagUiModel(
            id = UUID.randomUUID().toString(),
            name = category,
            color = color
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            tagRepository.addTags(newTag.toDomainModel())
                .onSuccess {
                    _event.trySend(SelectCategoryEvent.DismissDialog)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun updateChecked(tag: TagUiModel) {
        _uiState.update {
            val newList = if (tag in it.checkedCategories) {
                it.checkedCategories - tag
            } else {
                if (it.checkedCategories.size >= MAX_SELECTABLE_COUNT) {
                    _event.trySend(SelectCategoryEvent.ShowSnackBar(SelectCategoryError.MaxCategoryOverError))
                    return
                }
                it.checkedCategories + tag
            }

            it.copy(checkedCategories = newList)
        }
    }
}