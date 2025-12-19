package com.andone.memorip.presentation.selectgroup

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.SelectGroup

fun NavBackStack<NavKey>.navigateToSelectGroup() {
    add(SelectGroup)
}

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