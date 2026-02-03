package com.andone.memorip.presentation.screen.placedetail

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.PlaceDetail

fun NavBackStack<NavKey>.navigateToPlaceDetail(placeId: String) {
    add(element = PlaceDetail(placeId = placeId))
}

fun EntryProviderScope<NavKey>.placeDetail(
    onNavigateBack: () -> Unit,
    onNavigateToTripList: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceDetail> { route ->
        PlaceDetailContainer(
            route = route,
            onNavigateBack = onNavigateBack,
            onNavigateToTripList = onNavigateToTripList,
            modifier = modifier
        )
    }
}