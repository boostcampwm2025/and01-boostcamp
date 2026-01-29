package com.andone.memorip.presentation.screen.placelist

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.PlaceList

fun NavBackStack<NavKey>.navigateToPlaceList() {
    add(PlaceList)
}

fun EntryProviderScope<NavKey>.placeList(
    metadata: Map<String, Any>,
    onPlaceClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceList>(metadata = metadata) {
        PlaceListScreen(
            onPlaceClick = onPlaceClick,
            onCreatePlaceClick = onCreatePlaceClick,
            modifier = modifier,
        )
    }
}