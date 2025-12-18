package com.andone.memorip.presentation.groupdetail

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.GroupDetail

fun EntryProviderScope<NavKey>.groupDetail(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<GroupDetail> { backStackEntry ->
        GroupDetailScreen(
            onBackClick = onBackClick,
            modifier = modifier
        )
    }
}