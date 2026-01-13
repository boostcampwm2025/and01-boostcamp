package com.andone.memorip.presentation.screen.selectcategory.model

import com.andone.memorip.presentation.model.TagUiModel

sealed interface SelectCategoryEvent {

    data object NavigateBack : SelectCategoryEvent

    data class SelectCategory(val categories: List<TagUiModel>) : SelectCategoryEvent

    data object ShowDialog : SelectCategoryEvent

    data object DismissDialog : SelectCategoryEvent

    data class ShowSnackBar(val message: SelectCategoryError) : SelectCategoryEvent
}