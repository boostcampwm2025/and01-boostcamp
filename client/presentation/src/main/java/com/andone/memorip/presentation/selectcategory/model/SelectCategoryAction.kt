package com.andone.memorip.presentation.selectcategory.model

import androidx.compose.ui.graphics.Color
import com.andone.memorip.presentation.model.Category

sealed interface SelectCategoryAction {

    data class OnCategoryItemClick(
        val category: Category,
        val checked: Boolean
    ) : SelectCategoryAction

    data object OnFABClick : SelectCategoryAction

    data object OnBackClick : SelectCategoryAction

    data object OnConfirmClick : SelectCategoryAction

    data class OnDialogConfirmClick(
        val category: String,
        val color: Color
    ) : SelectCategoryAction

    data object OnDialogCancelClick : SelectCategoryAction
}