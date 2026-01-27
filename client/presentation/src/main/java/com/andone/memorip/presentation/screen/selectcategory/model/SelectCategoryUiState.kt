package com.andone.memorip.presentation.screen.selectcategory.model

import com.andone.memorip.presentation.model.TagUiModel

data class SelectCategoryUiState(
    val checkedCategories: List<TagUiModel> = emptyList(),
    val isLoading: Boolean = true
)