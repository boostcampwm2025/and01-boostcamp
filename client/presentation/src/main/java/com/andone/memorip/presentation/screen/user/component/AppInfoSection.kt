package com.andone.memorip.presentation.screen.user.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.user.model.SettingItemUiModel
import com.andone.memorip.presentation.screen.user.model.SettingTrailing
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun AppInfoSection(
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName

    val items = listOf(
        SettingItemUiModel(
            iconRes = R.drawable.ic_baseline_phone_android,
            title = stringResource(R.string.login_app_version),
            trailing = SettingTrailing.Text(text = versionName ?: ""),
            onClick = {},
            clickable = false
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
            onAction = {},
        )
    }
}