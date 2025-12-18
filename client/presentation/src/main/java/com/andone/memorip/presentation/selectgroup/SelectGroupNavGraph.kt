package com.andone.memorip.presentation.selectgroup

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.SelectGroup
import com.andone.memorip.presentation.selectcategory.SelectCategoryScreen

fun EntryProviderScope<NavKey>.selectGroup(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<SelectGroup> {
        SelectGroupScreen(
            onBackClick = onBackClick,
            modifier = modifier
        )
    }
}