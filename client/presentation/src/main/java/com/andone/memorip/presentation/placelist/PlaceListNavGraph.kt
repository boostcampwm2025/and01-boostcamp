package com.andone.memorip.presentation.placelist

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.PlaceList

fun EntryProviderScope<NavKey>.placeList(
    onPlaceClick: (String) -> Unit,
    onCreatePlaceClick: () -> Unit,
    modifier: Modifier = Modifier
){
    entry<PlaceList> {
        PlaceListScreen(
            onPlaceClick = onPlaceClick,
            onCreatePlaceClick = onCreatePlaceClick,
            modifier = modifier,
        )
    }
}