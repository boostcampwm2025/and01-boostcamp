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
fun PolicySection(
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        SettingItemUiModel(
            iconRes = R.drawable.ic_outline_description,
            title = stringResource(R.string.login_service_title),
            trailing = SettingTrailing.Arrow(justArrow = true),
            onClick = {}
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_outline_privacy,
            title = stringResource(R.string.login_privacy_title),
            trailing = SettingTrailing.Arrow(justArrow = true),
            onClick = {}
        ),
    )
    SettingSection(
        title = stringResource(R.string.login_policy),
        items = items,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun PolicySectionPreview() {
    MemoripTheme {
        PolicySection(
            onAction = {},
        )
    }
}