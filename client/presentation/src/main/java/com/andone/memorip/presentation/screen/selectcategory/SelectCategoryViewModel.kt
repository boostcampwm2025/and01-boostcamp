package com.andone.memorip.presentation.screen.selectcategory

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.screen.selectcategory.Constants.MAX_SELECTABLE_COUNT
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryAction
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryError
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryEvent
import com.andone.memorip.presentation.screen.selectcategory.model.SelectCategoryUiState
import com.andone.memorip.presentation.util.DummyData.categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.UUID
import javax.inject.Inject

private object Constants {
    const val MAX_SELECTABLE_COUNT = 3
}

@HiltViewModel
class SelectCategoryViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SelectCategoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SelectCategoryEvent>(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        // 더미데이터 사용
        _uiState.value = SelectCategoryUiState(categories = categories.toImmutableList())
    }

    fun onAction(action: SelectCategoryAction) {
        when (action) {
            SelectCategoryAction.OnBackClick -> {
                _event.trySend(element = SelectCategoryEvent.NavigateBack)
            }

            is SelectCategoryAction.OnConfirmClick -> {
                _event.trySend(
                    element = SelectCategoryEvent.SelectCategory(
                        categories = _uiState.value.categories.filter { it.id in _uiState.value.checkedSet }
                    )
                )
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

        val categories = _uiState.value.categories + newTag
        _uiState.value = _uiState.value.copy(categories = categories.toImmutableList())
        _event.trySend(SelectCategoryEvent.DismissDialog)
    }

    private fun updateChecked(tag: TagUiModel) {
        val checkedSet = if (tag.id in _uiState.value.checkedSet) {
            _uiState.value.checkedSet - tag.id
        } else {
            if (_uiState.value.checkedSet.size >= MAX_SELECTABLE_COUNT) {
                _event.trySend(element = SelectCategoryEvent.ShowSnackBar(message = SelectCategoryError.MaxCategoryOverError))
                return
            }
            _uiState.value.checkedSet + tag.id
        }
        _uiState.value = _uiState.value.copy(checkedSet = checkedSet.toImmutableSet())
    }
}