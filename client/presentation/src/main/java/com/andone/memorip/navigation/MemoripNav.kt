package com.andone.memorip.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.andone.memorip.navigation.MemoripNavConstant.tabTransition
import com.andone.memorip.presentation.screen.tripdetail.tripDetail
import com.andone.memorip.presentation.screen.triplist.tripList
import com.andone.memorip.presentation.screen.placecreate.placeCreate
import com.andone.memorip.presentation.screen.placedetail.placeDetail
import com.andone.memorip.presentation.screen.placelist.placeList
import com.andone.memorip.presentation.screen.plan.plan
import com.andone.memorip.presentation.screen.user.user

private object MemoripNavConstant {
    val tabTransition = NavDisplay.transitionSpec { fadeIn() togetherWith fadeOut() }
}

@Composable
fun MemoripNav(
    navigator: MemoripNavigator,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavDisplay(
        backStack = navigator.backStack,
        onBack = navigator::popBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        entryProvider = entryProvider {
            placeList(
                metadata = tabTransition,
                onPlaceClick = navigator::navigateToPlaceDetail,
                onCreatePlaceClick = navigator::navigateToPlaceCreate,
                modifier = modifier.padding(paddingValues = innerPadding)
            )

            tripList(
                metadata = tabTransition,
                onTripClick = { tripId -> navigator.navigateToTripDetail(tripId) },
                onCreatePlaceClick = navigator::navigateToPlaceCreate,
                modifier = modifier.padding(paddingValues = innerPadding),
            )

            plan(
                metadata = tabTransition,
                modifier = modifier.padding(paddingValues = innerPadding)
            )

            user(
                metadata = tabTransition,
                modifier = modifier.padding(paddingValues = innerPadding)
            )

            placeCreate(
                onNavigateToHome = { navigator.navigateToTab(MainBottomBarRoute.PLACE_LIST) },
                onBackClick = navigator::popBackStack
            )

            tripDetail(
                onNavigateBack = navigator::popBackStack,
                onNavigateToPlaceDetail = navigator::navigateToPlaceDetail
            )

            placeDetail(
                onNavigateBack = navigator::popBackStack,
                onNavigateToTripList = navigator::navigateToTripList
            )
        }
    )
}