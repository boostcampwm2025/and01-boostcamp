package com.andone.memorip.presentation.selectcategory.model

sealed interface SelectCategoryEvent {

    data object NavigateBack : SelectCategoryEvent

    data class NavigateAddPlace(val categories: List<Category>) : SelectCategoryEvent

    data object ShowDialog : SelectCategoryEvent

    data object DismissDialog : SelectCategoryEvent

    data class ShowSnackBar(val message: SelectCategoryError) : SelectCategoryEvent
}