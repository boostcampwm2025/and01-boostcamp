package com.andone.memorip.presentation.groupdetail

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.GroupDetail

fun NavBackStack<NavKey>.navigateToGroupDetail(groupId: Int) {
    add(GroupDetail(groupId))
}

fun EntryProviderScope<NavKey>.groupDetail(
    onNavigateBack: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    entry<GroupDetail> { route ->
        GroupDetailScreen(
            onBackClick = onNavigateBack,
            onImageClick = onImageClick,
            modifier = modifier
        )
    }
}