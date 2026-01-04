package com.andone.memorip.presentation.grouplist

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.GroupList

fun EntryProviderScope<NavKey>.groupList(
    onGroupClick: (Int) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<GroupList> {
        GroupListScreen(
            onGroupClick = onGroupClick,
            onCreatePlaceClick = onCreatePlaceClick,
            modifier = modifier
        )
    }
}