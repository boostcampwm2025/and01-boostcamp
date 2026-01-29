package com.andone.memorip.presentation.screen.user.component

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import android.provider.Settings
import androidx.compose.ui.platform.LocalContext
import com.andone.memorip.presentation.util.openAppSettings

@Composable
fun PermissionSection(
    permissionUiState: PermissionUiState,
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val locationPermissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
        onAction(UserAction.OnLocationPermissionResult(granted))
    }

    val items = listOf(
        SettingItemUiModel(
            iconRes = R.drawable.ic_location_on,
            title = stringResource(R.string.login_permission_location_title),
            subtitle = stringResource(R.string.login_permission_location_subtitle),
            trailing = SettingTrailing.Arrow(isAllowed = permissionUiState.locationPermission),
            onClick = {
                if (permissionUiState.locationPermission) {
                    openAppSettings(context)
                } else {
                    locationPermissionLauncher.launch(input = Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        ),
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