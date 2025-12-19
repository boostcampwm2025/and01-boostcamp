package com.andone.memorip.presentation.placedetail

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.PlaceDetail

fun NavBackStack<NavKey>.navigateToPlaceDetail(placeId: Int) {
    add(PlaceDetail(placeId))
}

fun EntryProviderScope<NavKey>.placeDetail(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceDetail> { route ->
        PlaceDetailScreen(
            route = route,
            onNavigateBack = onNavigateBack,
            modifier = modifier
        )
    }
}