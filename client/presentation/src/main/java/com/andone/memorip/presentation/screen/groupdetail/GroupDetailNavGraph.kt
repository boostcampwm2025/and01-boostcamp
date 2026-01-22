package com.andone.memorip.presentation.screen.groupdetail

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.andone.memorip.navigation.GroupDetail

fun NavBackStack<NavKey>.navigateToGroupDetail(groupId: String) {
    add(GroupDetail(groupId))
}

fun EntryProviderScope<NavKey>.groupDetail(
    onNavigateBack: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    entry<GroupDetail>(
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
        GroupDetailScreen(
            route = route,
            onBackClick = onNavigateBack,
            onImageClick = onImageClick,
            modifier = modifier
        )
    }
}