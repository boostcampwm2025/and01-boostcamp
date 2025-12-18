package com.andone.memorip.presentation.place

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.PlaceCreate


fun EntryProviderScope<NavKey>.placeCreate(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    entry<PlaceCreate> {
        PlaceCreateScreen(
            onBackClick = onBackClick
        )
    }
}