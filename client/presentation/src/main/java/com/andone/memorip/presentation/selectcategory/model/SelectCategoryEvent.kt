package com.andone.memorip.presentation.selectcategory.model

interface SelectCategoryEvent {
    data object onNavigateBack: SelectCategoryEvent
    data class onNavigateAddPlace(val categories: List<Category>): SelectCategoryEvent
    data object onShowDialog: SelectCategoryEvent
    data object onDismissDialog: SelectCategoryEvent
    data class onShowSnackbar(val message: SelectCategoryError): SelectCategoryEvent
}