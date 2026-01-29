package com.andone.memorip.presentation.screen.grouplist

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.GroupList

fun NavBackStack<NavKey>.navigateToGroupList() {
    add(GroupList)
}

fun EntryProviderScope<NavKey>.groupList(
    metadata: Map<String, Any>,
    onGroupClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<GroupList>(metadata = metadata) {
        GroupListScreen(
            onGroupClick = onGroupClick,
            onCreatePlaceClick = onCreatePlaceClick,
            modifier = modifier
        )
    }
}