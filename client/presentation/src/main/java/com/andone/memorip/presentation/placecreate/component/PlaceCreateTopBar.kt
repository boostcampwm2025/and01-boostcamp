package com.andone.memorip.presentation.placecreate.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceCreateTopBar(
    onBackClick: () -> Unit,
    onConfirmClick: () -> Unit = {},
    confirmEnabled: Boolean = false
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.place_create_title)) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_outline_arrow_back),
                    contentDescription = stringResource(
                        R.string.place_create_back_content_description
                    )
                )
            }
        },
        actions = {
            IconButton(
                onClick = onConfirmClick,
                enabled = confirmEnabled
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = stringResource(
                        R.string.place_create_check_content_description
                    )
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MemoripTheme.colors.primaryContainer)
    )
}

@Preview
@Composable
private fun PlaceCreateTopBarPreview(){
    MemoripTheme {
        PlaceCreateTopBar(
            onBackClick = {},
            onConfirmClick = {}
        )
    }
}
