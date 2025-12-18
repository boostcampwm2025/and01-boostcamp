package com.andone.memorip.presentation.home

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.Home
import com.andone.memorip.navigation.PlaceCreate
import com.andone.memorip.presentation.place.PlaceCreateScreen

fun EntryProviderScope<NavKey>.home(
    onCreateGroupClick: () -> Unit,
    onGroupClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<Home> {
        HomeScreen(
            onCreateGroupClick = onCreateGroupClick,
            onGroupClick = onGroupClick,
            onCreatePlaceClick = onCreatePlaceClick,
            modifier = modifier
        )
    }
}