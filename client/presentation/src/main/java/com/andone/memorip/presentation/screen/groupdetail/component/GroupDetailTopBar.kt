package com.andone.memorip.presentation.screen.groupdetail.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailTopBar(
    title: String,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MemoripTheme.typography.headlineBold20,
                color = MemoripTheme.colors.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.groupdetail_back_button_content_description)
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(R.string.groupdetail_search_button_content_description)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MemoripTheme.colors.background,
            navigationIconContentColor = MemoripTheme.colors.onSurface,
            titleContentColor = MemoripTheme.colors.onSurface,
            actionIconContentColor = MemoripTheme.colors.onSurface
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun GroupDetailTopBarPreview() {
    MemoripTheme {
        GroupDetailTopBar(
            title = "Group1",
            onBackClick = {},
            onSearchClick = {},
            onMenuClick = {}
        )
    }
}