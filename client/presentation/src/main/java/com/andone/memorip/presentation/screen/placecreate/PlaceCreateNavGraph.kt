package com.andone.memorip.presentation.screen.placecreate

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.andone.memorip.navigation.PlaceCreate

fun NavBackStack<NavKey>.navigateToPlaceCreate() {
    add(PlaceCreate)
}

fun EntryProviderScope<NavKey>.placeCreate(
    onNavigateToHome: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceCreate>(
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
    ) {
        PlaceCreateContainer(
            modifier = modifier,
            onNavigateToHome = onNavigateToHome,
            onBackClick = onBackClick
        )
    }
}