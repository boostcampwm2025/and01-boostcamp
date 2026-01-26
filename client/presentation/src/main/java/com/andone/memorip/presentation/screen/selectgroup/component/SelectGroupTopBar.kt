package com.andone.memorip.presentation.screen.selectgroup.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGroupTopBar(
    onBackClick: () -> Unit,
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.select_group_title),
    hasSelectedGroups: Boolean = false
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MemoripTheme.typography.headlineBold20
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.select_group_back_button_description)
                )
            }
        },
        actions = {
            IconButton(onClick = onCheckClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                    contentDescription = stringResource(R.string.select_group_check_button_description),
                    tint = if (hasSelectedGroups) {
                        MemoripTheme.colors.primary
                    } else {
                        MemoripTheme.colors.lightGray
                    }
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MemoripTheme.colors.background,
            navigationIconContentColor = MemoripTheme.colors.onSurface,
            titleContentColor = MemoripTheme.colors.onSurface
        )
    )
}

@Composable
@Preview
private fun SelectGroupTopBarPreview() {
    MemoripTheme {
        SelectGroupTopBar(
            onBackClick = {},
            onCheckClick = {},
            hasSelectedGroups = false
        )
    }
}
