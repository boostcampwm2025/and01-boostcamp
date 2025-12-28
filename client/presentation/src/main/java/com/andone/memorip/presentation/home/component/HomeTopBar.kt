package com.andone.memorip.presentation.home.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onSearchClick: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(
                        R.string.home_search_content_description
                    )
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MemoripTheme.colors.primaryContainer)
    )
}

@Preview
@Composable
private fun HomeTopBarPreview(){
    MemoripTheme {
        HomeTopBar(onSearchClick = {})
    }
}