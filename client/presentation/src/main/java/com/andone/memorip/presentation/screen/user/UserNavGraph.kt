package com.andone.memorip.presentation.screen.user

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.User

fun EntryProviderScope<NavKey>.user(modifier: Modifier = Modifier) {
    entry<User> {
        UserScreen(modifier = modifier)
    }
}