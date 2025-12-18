package com.andone.memorip.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.andone.memorip.presentation.groupdetail.groupDetail
import com.andone.memorip.presentation.home.home
import com.andone.memorip.presentation.placecreate.placeCreate
import com.andone.memorip.presentation.placedetail.placeDetail
import com.andone.memorip.presentation.selectcategory.selectCategory
import com.andone.memorip.presentation.selectgroup.selectGroup

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
            home(
                onCreateGroupClick = {},
                onGroupClick = { groupId -> navigator.navigateToGroupDetail(groupId) },
                onCreatePlaceClick = navigator::navigateToCreatePlace,
                modifier = modifier.padding(paddingValues = innerPadding),
            )

            entry<User> { Text(text = "user") }

            placeCreate(onBackClick = navigator::popBackStack)

            selectGroup(onBackClick = navigator::popBackStack)

            selectCategory(onBackClick = navigator::popBackStack)

            groupDetail(
                onNavigateBack = navigator::popBackStack,
//                onCreatePlaceClick = {navigator.navigateToCreatePlace()},
            )

            placeDetail(onNavigateBack = navigator::popBackStack)
        },
    )
}