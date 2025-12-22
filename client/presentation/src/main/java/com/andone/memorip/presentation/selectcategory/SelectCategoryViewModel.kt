package com.andone.memorip.presentation.selectcategory

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.selectcategory.Constants.MAX_SELECTABLE_COUNT
import com.andone.memorip.presentation.selectcategory.model.Category
import com.andone.memorip.presentation.selectcategory.model.SelectCategoryAction
import com.andone.memorip.presentation.selectcategory.model.SelectCategoryError
import com.andone.memorip.presentation.selectcategory.model.SelectCategoryEvent
import com.andone.memorip.presentation.selectcategory.model.SelectCategoryUiState
import com.andone.memorip.presentation.util.DummyData.categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

private object Constants {
    val MAX_SELECTABLE_COUNT = 3
}

@HiltViewModel
class SelectCategoryViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<SelectCategoryUiState> =
        MutableStateFlow(SelectCategoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _event: Channel<SelectCategoryEvent> = Channel(capacity = BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        _uiState.value = SelectCategoryUiState(categories = categories.toImmutableList())
    }

    fun onAction(action: SelectCategoryAction) {
        when (action) {
            SelectCategoryAction.onBackClick -> {
                _event.trySend(element = SelectCategoryEvent.onNavigateBack)
            }

            is SelectCategoryAction.onConfirmClick -> {
                _event.trySend(
                    element = SelectCategoryEvent.onNavigateAddPlace(
                        categories = uiState.value.categories.filter { it.id in uiState.value.checkedSet }
                    )
                )
            }

            SelectCategoryAction.onCancelDialogClick -> {
                _event.trySend(element = SelectCategoryEvent.onDismissDialog)
            }

            is SelectCategoryAction.onCategoryItemClick -> {
                updateChecked(category = action.category)
            }

            is SelectCategoryAction.onConfirmDialogClick -> {
                addCategory(
                    category = action.category,
                    color = action.color
                )
            }

            SelectCategoryAction.onFABClick -> {
                _event.trySend(element = SelectCategoryEvent.onShowDialog)
            }
        }
    }

    private fun addCategory(category: String, color: Color) {
        val maxId = uiState.value.categories.maxOfOrNull { it.id } ?: -2L
        val newCategory = Category(
            id = maxId + 1L,
            category = category,
            color = color
        )

        val categories = uiState.value.categories + newCategory
        _uiState.value = uiState.value.copy(categories = categories.toImmutableList())
        _event.trySend(SelectCategoryEvent.onDismissDialog)
    }

    private fun updateChecked(category: Category) {
        val checkedSet = if (category.id in uiState.value.checkedSet) {
            uiState.value.checkedSet - category.id
        } else {
            if (uiState.value.checkedSet.size >= MAX_SELECTABLE_COUNT) {
                _event.trySend(
                    element = SelectCategoryEvent.onShowSnackbar(message = SelectCategoryError.MaxCategoryOverError)
                )
                return
            }

            uiState.value.checkedSet + category.id
        }

        _uiState.value = uiState.value.copy(checkedSet = checkedSet.toImmutableSet())
    }
}