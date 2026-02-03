package com.andone.memorip.presentation.screen.user.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.user.model.SettingItemUiModel
import com.andone.memorip.presentation.screen.user.model.SettingTrailing
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun AccountSection(
    showLogoutDialog: () -> Unit,
    showDeleteAccountDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        SettingItemUiModel(
            iconRes = R.drawable.ic_baseline_logout,
            title = stringResource(R.string.login_logout),
            trailing = SettingTrailing.Arrow(justArrow = true),
            onClick = showLogoutDialog
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_outline_person_cancel,
            title = stringResource(R.string.login_delete_account),
            trailing = SettingTrailing.Arrow(justArrow = true),
            onClick = showDeleteAccountDialog
        ),
    )
    SettingSection(
        title = stringResource(R.string.login_account),
        items = items,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun AccountSectionPreview() {
    MemoripTheme {
        AccountSection(
            showLogoutDialog = {},
            showDeleteAccountDialog = {},
        )
    }
}