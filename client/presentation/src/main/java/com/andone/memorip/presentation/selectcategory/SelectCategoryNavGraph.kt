package com.andone.memorip.presentation.selectcategory

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.SelectCategory

fun EntryProviderScope<NavKey>.selectCategory(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<SelectCategory> {
        SelectCategoryScreen(
            onBackClick = onBackClick,
            modifier = modifier
        )
    }
}