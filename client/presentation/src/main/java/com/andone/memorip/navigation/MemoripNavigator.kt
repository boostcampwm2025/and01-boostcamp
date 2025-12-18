package com.andone.memorip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
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

    fun navigateToCreatePlace(){
        backStack.add(PlaceCreate)
    }

    fun popBackStack() = backStack.removeLastOrNull()
}

@Composable
fun rememberMemoripNavigator(
    backStack: NavBackStack<NavKey> = rememberNavBackStack(MainBottomBarRoute.HOME.route)
): MemoripNavigator = remember(backStack) {
    MemoripNavigator(backStack)
}