package com.andone.memorip.presentation.selectcategory.model

import androidx.compose.ui.graphics.Color

sealed interface SelectCategoryAction {
    data class onCategoryItemClick(
        val category: Category,
        val checked: Boolean
    ): SelectCategoryAction
    data object onFABClick: SelectCategoryAction
    data object onBackClick: SelectCategoryAction
    data object onConfirmClick: SelectCategoryAction
    data class onConfirmDialogClick(
        val category: String,
        val color: Color
    ): SelectCategoryAction
    data object onCancelDialogClick: SelectCategoryAction
}