package com.andone.memorip.presentation.grouplist.component

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun GroupListFloatingActionButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MemoripTheme.colors.primaryContainer,
        contentColor = MemoripTheme.colors.black
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(R.string.group_list_add_content_description)
        )
    }
}

@Preview
@Composable
private fun GroupListFloatingActionButtonPreview() {
    MemoripTheme {
        GroupListFloatingActionButton(onClick = {})
    }
}