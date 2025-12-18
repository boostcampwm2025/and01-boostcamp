package com.andone.memorip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.andone.memorip.presentation.groupdetail.navigateToGroupDetail
import com.andone.memorip.presentation.placecreate.navigateToPlaceCreate
import com.andone.memorip.presentation.placedetail.navigateToPlaceDetail
import com.andone.memorip.presentation.selectcategory.navigateToSelectCategory
import com.andone.memorip.presentation.selectgroup.navigateToSelectGroup
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
        backStack.add(tab.route)
    }

    fun navigateToPlaceDetail(placeId: Long) = backStack.navigateToPlaceDetail(placeId)

    fun navigateToSelectCategory() = backStack.navigateToSelectCategory()

    fun navigateToSelectGroup() = backStack.navigateToSelectGroup()

    fun navigateToGroupDetail(groupId: String) = backStack.navigateToGroupDetail(groupId)

    fun navigateToCreatePlace() = backStack.navigateToPlaceCreate()

    fun popBackStack() = backStack.removeLastOrNull()
}

@Composable
fun rememberMemoripNavigator(
    backStack: NavBackStack<NavKey> = rememberNavBackStack(MainBottomBarRoute.HOME.route)
): MemoripNavigator = remember(backStack) {
    MemoripNavigator(backStack)
}