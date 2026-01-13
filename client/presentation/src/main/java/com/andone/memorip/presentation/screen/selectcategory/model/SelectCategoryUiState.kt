package com.andone.memorip.presentation.screen.selectcategory.model

import com.andone.memorip.presentation.model.TagUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

data class SelectCategoryUiState(
    val categories: ImmutableList<TagUiModel> = emptyList<TagUiModel>().toImmutableList(),
    val checkedSet: ImmutableSet<String> = emptySet<String>().toImmutableSet()
)