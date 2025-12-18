package com.andone.memorip.presentation.placecreate

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.PlaceCreate

fun NavBackStack<NavKey>.navigateToPlaceCreate() {
    add(PlaceCreate)
}

fun EntryProviderScope<NavKey>.placeCreate(
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onGroupClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceCreate> {
        PlaceCreateScreen(
            onCategoryClick = onCategoryClick,
            onLocationClick = onLocationClick,
            onGroupClick = onGroupClick,
            onBackClick = onBackClick,
            modifier = modifier
        )
    }
}