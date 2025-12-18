package com.andone.memorip.presentation.groupdetail

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.GroupDetail

fun NavBackStack<NavKey>.navigateToGroupDetail(groupId: String) {
    add(GroupDetail(groupId))
}

fun EntryProviderScope<NavKey>.groupDetail(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<GroupDetail> { route ->
        GroupDetailScreen(
            onBackClick = onNavigateBack,
            modifier = modifier
        )
    }
}