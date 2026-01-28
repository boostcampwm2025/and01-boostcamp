package com.andone.memorip.presentation.screen.user.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.user.model.PermissionUiState
import com.andone.memorip.presentation.screen.user.model.SettingItemUiModel
import com.andone.memorip.presentation.screen.user.model.SettingTrailing
import com.andone.memorip.presentation.screen.user.model.UserAction

@Composable
fun PermissionSection(
    permissionUiState: PermissionUiState,
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        SettingItemUiModel(
            iconRes = R.drawable.ic_baseline_camera_alt,
            title = stringResource(R.string.permission_camera_title),
            subtitle = stringResource(R.string.permission_camera_subtitle),
            trailing = SettingTrailing.Arrow(isAllowed = permissionUiState.cameraPermission),
            onClick = {}
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_outline_gallery_thumbnail,
            title = stringResource(R.string.permission_gallery_title),
            subtitle = stringResource(R.string.permission_gallery_subtitle),
            trailing = SettingTrailing.Arrow(isAllowed = permissionUiState.galleryPermission),
            onClick = {}
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_location_on,
            title = stringResource(R.string.permission_location_title),
            subtitle = stringResource(R.string.permission_location_subtitle),
            trailing = SettingTrailing.Arrow(isAllowed = permissionUiState.locationPermission),
            onClick = {}
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_outline_android_wifi_3_bar,
            title = stringResource(R.string.permission_network_title),
            subtitle = stringResource(R.string.permission_network_subtitle),
            trailing = SettingTrailing.Arrow(isAllowed = permissionUiState.networkPermission),
            onClick = {}
        )
    )
    SettingSection(
        title = stringResource(R.string.login_permission),
        items = items,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun PermissionSectionPreview() {
    MemoripTheme {
        PermissionSection(
            PermissionUiState(),
            onAction = {},
        )
    }
}