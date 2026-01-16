package com.andone.memorip.presentation.screen.selectcategory.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.LocalMemoripColors
import com.andone.memorip.presentation.theme.LocalMemoripTypography
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCategoryTopBar(
    checkEnabled: Boolean,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.select_category_title),
                style = LocalMemoripTypography.current.headline2
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
            IconButton(
                onClick = onConfirmClick,
                enabled = checkEnabled,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_check),
                    contentDescription = stringResource(R.string.select_category_confirm_button_description)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalMemoripColors.current.background,
            navigationIconContentColor = MemoripTheme.colors.onSurface,
            titleContentColor = MemoripTheme.colors.onSurface
        )
    )
}