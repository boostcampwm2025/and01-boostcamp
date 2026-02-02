package com.andone.memorip.presentation.screen.selectcategory.model

import androidx.compose.ui.graphics.Color
import com.andone.memorip.presentation.model.TagUiModel

sealed interface SelectCategoryAction {

    data class OnInitialTags(val tags: List<TagUiModel>) : SelectCategoryAction

    data class OnCategoryItemClick(
        val tagUiModel: TagUiModel,
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