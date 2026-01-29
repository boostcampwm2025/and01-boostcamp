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
fun AppInfoSection(
    appVersion: String,
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        SettingItemUiModel(
            iconRes = R.drawable.ic_baseline_phone_android,
            title = stringResource(R.string.login_app_version),
            trailing = SettingTrailing.Text(text = appVersion),
            onClick = {}
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_baseline_autorenew,
            title = stringResource(R.string.login_app_version_check),
            trailing = SettingTrailing.Arrow(justArrow = true),
            onClick = {}
        ),
    )
    SettingSection(
        title = stringResource(R.string.login_app_info),
        items = items,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun AppInfoSectionPreview() {
    MemoripTheme {
        AppInfoSection(
            appVersion = "2.1.2",
            onAction = {},
        )
    }
}