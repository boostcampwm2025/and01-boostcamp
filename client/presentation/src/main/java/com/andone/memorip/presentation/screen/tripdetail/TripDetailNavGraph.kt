package com.andone.memorip.presentation.screen.tripdetail

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.TripDetail

fun NavBackStack<NavKey>.navigateToTripDetail(tripId: String) {
    add(TripDetail(tripId))
}

fun EntryProviderScope<NavKey>.tripDetail(
    onNavigateBack: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    entry<TripDetail> { route ->
        TripDetailScreen(
            route = route,
            onBackClick = onNavigateBack,
            onImageClick = onImageClick,
            modifier = modifier
        )
    }
}