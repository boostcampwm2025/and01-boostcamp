package com.andone.memorip.presentation.home

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.Home

fun EntryProviderScope<NavKey>.home(
    onGroupClick: (String) -> Unit,
    onCreateGroupClick: () -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<Home> {
        HomeScreen(
            onGroupClick = onGroupClick,
            onCreateGroupClick = onCreateGroupClick,
            onCreatePlaceClick = onCreatePlaceClick,
            modifier = modifier
        )
    }
}