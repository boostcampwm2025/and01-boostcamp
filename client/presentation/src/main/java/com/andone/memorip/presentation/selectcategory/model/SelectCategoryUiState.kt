package com.andone.memorip.presentation.selectcategory.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

data class SelectCategoryUiState(
    val categories: ImmutableList<Category> = emptyList<Category>().toImmutableList(),
    val checkedSet: ImmutableSet<Long> = emptySet<Long>().toImmutableSet()
)