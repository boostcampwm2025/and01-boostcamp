package com.andone.memorip.presentation.placelist.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PlaceListTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    LargeTopAppBar(
        title = { },
        scrollBehavior = scrollBehavior
    )
}
