package com.andone.memorip.presentation.screen.user.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.user.model.AlarmUiState
import com.andone.memorip.presentation.screen.user.model.SettingItemUiModel
import com.andone.memorip.presentation.screen.user.model.SettingTrailing
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun AlarmSection(
    alarmUiState: AlarmUiState,
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        SettingItemUiModel(
            iconRes = R.drawable.ic_outline_circle_notifications,
            title = stringResource(R.string.login_push_alarm_title),
            subtitle = stringResource(R.string.login_push_alarm_sub_title),
            trailing = SettingTrailing.Toggle(
                checked = alarmUiState.pushAlarm,
                onCheckedChange = {}
            ),
            onClick = {},
            clickable = false
        ),
        SettingItemUiModel(
            iconRes = R.drawable.ic_outline_bookmark_add,
            title = stringResource(R.string.login_scrap_alarm_title),
            subtitle = stringResource(R.string.login_scrap_alarm_sub_title),
            trailing = SettingTrailing.Toggle(
                checked = alarmUiState.scrapAlarm,
                onCheckedChange = {}
            ),
            onClick = {},
            clickable = false
        ),
    )
    SettingSection(
        title = stringResource(R.string.login_alarm),
        items = items,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun AlarmSectionPreview() {
    MemoripTheme {
        AlarmSection(
            AlarmUiState(),
            onAction = {},
        )
    }
}