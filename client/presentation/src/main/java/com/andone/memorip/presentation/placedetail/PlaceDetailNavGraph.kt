package com.andone.memorip.presentation.placedetail

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.PlaceDetailRoute

fun NavBackStack<NavKey>.navigateToPlaceDetail(id: Long) {
    add(PlaceDetailRoute(id))
}

fun EntryProviderScope<NavKey>.placeDetail(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceDetailRoute> { route ->
        PlaceDetailScreen(
            route = route,
            onNavigateBack = onNavigateBack,
            modifier = modifier
        )
    }
}