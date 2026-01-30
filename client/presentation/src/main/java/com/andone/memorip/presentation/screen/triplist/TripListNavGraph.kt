package com.andone.memorip.presentation.screen.triplist

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.TripList

fun NavBackStack<NavKey>.navigateToTripList() {
    add(TripList)
}

fun EntryProviderScope<NavKey>.tripList(
    metadata: Map<String, Any>,
    onTripClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<TripList>(metadata = metadata) {
        TripListScreen(
            onTripClick = onTripClick,
            onCreatePlaceClick = onCreatePlaceClick,
            modifier = modifier
        )
    }
}