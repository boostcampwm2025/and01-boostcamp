package com.andone.memorip.presentation.screen.placelist.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceListTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    query: String,
    onQueryChange: (String) -> Unit,
) {
    TopAppBar(
        title = {
            SearchTextField(
                value = query,
                onValueChange = onQueryChange
            )
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MemoripTheme.colors.offWhite,
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun PlaceListTopBarPreview() {
    MemoripTheme {
        PlaceListTopBar(
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            query = "검색",
            onQueryChange = {}
        )
    }
}
