package com.andone.memorip.presentation.screen.placedetail

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.andone.memorip.navigation.PlaceDetail

fun NavBackStack<NavKey>.navigateToPlaceDetail(placeId: String) {
    add(element = PlaceDetail(placeId = placeId))
}

fun EntryProviderScope<NavKey>.placeDetail(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceDetail>(
        metadata = NavDisplay.transitionSpec {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        } + NavDisplay.popTransitionSpec {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        } + NavDisplay.predictivePopTransitionSpec {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }
    ) { route ->
        PlaceDetailScreen(
            route = route,
            onNavigateBack = onNavigateBack,
            modifier = modifier
        )
    }
}