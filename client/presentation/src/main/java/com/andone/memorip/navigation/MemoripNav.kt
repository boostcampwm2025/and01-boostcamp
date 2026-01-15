package com.andone.memorip.navigation

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
import com.andone.memorip.presentation.screen.groupdetail.groupDetail
import com.andone.memorip.presentation.screen.grouplist.groupList
import com.andone.memorip.presentation.screen.placecreate.placeCreate
import com.andone.memorip.presentation.screen.placedetail.placeDetail
import com.andone.memorip.presentation.screen.placelist.placeList
import com.andone.memorip.presentation.screen.user.user

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
                onPlaceClick = navigator::navigateToPlaceDetail,
                onCreatePlaceClick = navigator::navigateToPlaceCreate,
                modifier = modifier.padding(paddingValues = innerPadding)
            )

            groupList(
                onGroupClick = { groupId -> navigator.navigateToGroupDetail(groupId) },
                onCreatePlaceClick = navigator::navigateToPlaceCreate,
                modifier = modifier.padding(paddingValues = innerPadding),
            )

            user(modifier = modifier.padding(paddingValues = innerPadding))

            placeCreate(onBackClick = navigator::popBackStack)

            groupDetail(
                onNavigateBack = navigator::popBackStack,
                onImageClick = navigator::navigateToPlaceDetail
            )

            placeDetail(onNavigateBack = navigator::popBackStack)
        },
    )
}