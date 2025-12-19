package com.andone.memorip.presentation.selectlocation

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.SelectLocation

fun NavBackStack<NavKey>.navigateToSelectLocation() {
    add(SelectLocation)
}

fun EntryProviderScope<NavKey>.selectLocation(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<SelectLocation> {
        SelectLocationScreen(
            onBackClick = onBackClick,
            modifier = modifier
        )
    }
}