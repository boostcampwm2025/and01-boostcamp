package com.andone.memorip.presentation.screen.placecreate

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.PlaceCreate

fun NavBackStack<NavKey>.navigateToPlaceCreate() {
    add(PlaceCreate)
}

fun EntryProviderScope<NavKey>.placeCreate(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceCreate> {
        PlaceCreateContainer(
            modifier = modifier,
            onBackClick = onBackClick
        )
    }
}