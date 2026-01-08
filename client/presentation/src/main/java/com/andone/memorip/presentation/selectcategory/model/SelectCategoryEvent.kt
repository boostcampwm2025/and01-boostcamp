package com.andone.memorip.presentation.selectcategory.model

import com.andone.memorip.presentation.model.TagUiModel

sealed interface SelectCategoryEvent {

    data object NavigateBack : SelectCategoryEvent

    data class SelectCategory(val categories: List<TagUiModel>) : SelctCategoryEvent

    data object ShowDialog : SelectCategoryEvent

    data object DismissDialog : SelectCategoryEvent

    data class ShowSnackBar(val message: SelectCategoryError) : SelectCategoryEvent
}