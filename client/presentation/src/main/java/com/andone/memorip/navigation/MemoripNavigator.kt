package com.andone.memorip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.andone.memorip.presentation.screen.groupdetail.navigateToGroupDetail
import com.andone.memorip.presentation.screen.grouplist.navigateToGroupList
import com.andone.memorip.presentation.screen.placecreate.navigateToPlaceCreate
import com.andone.memorip.presentation.screen.placedetail.navigateToPlaceDetail
import com.andone.memorip.presentation.screen.placelist.navigateToPlaceList
import kotlinx.collections.immutable.toImmutableList

@Stable
class MemoripNavigator(
    val backStack: NavBackStack<NavKey>
) {
    val currentDestination: Any?
        get() = backStack.lastOrNull()

    val mainBottomBars = MainBottomBarRoute.entries.toImmutableList()

    val currentTab: MainBottomBarRoute?
        get() = MainBottomBarRoute.entries.find { tab ->
            currentDestination == tab.route
        }

    val isShowBottomBar: Boolean
        get() = currentTab != null

    fun navigateToTab(tab: MainBottomBarRoute) {
        if (currentTab == tab) return

        backStack.clear()

        if (tab != MainBottomBarRoute.PLACE_LIST) {
            navigateToPlaceList()
        }

        backStack.add(tab.route)
    }

    fun navigateToPlaceList() = backStack.navigateToPlaceList()

    fun navigateToGroupList() = backStack.navigateToGroupList()

    fun navigateToPlaceCreate() = backStack.navigateToPlaceCreate()

    fun navigateToGroupDetail(groupId: String) = backStack.navigateToGroupDetail(groupId)

    fun navigateToPlaceDetail(placeId: String) = backStack.navigateToPlaceDetail(placeId)
    fun popBackStack() = backStack.removeLastOrNull()
}

@Composable
fun rememberMemoripNavigator(
    backStack: NavBackStack<NavKey> = rememberNavBackStack(MainBottomBarRoute.PLACE_LIST.route)
): MemoripNavigator = remember(backStack) {
    MemoripNavigator(backStack)
}