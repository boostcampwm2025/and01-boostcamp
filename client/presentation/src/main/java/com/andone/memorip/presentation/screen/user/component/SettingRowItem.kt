package com.andone.memorip.presentation.screen.user.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.user.component.SettingRowItemDimen.MIN_HEIGHT
import com.andone.memorip.presentation.screen.user.component.SettingRowItemDimen.ROW_VERTICAL_PADDING
import com.andone.memorip.presentation.screen.user.component.SettingRowItemDimen.TRAILING_ICON_PADDING
import com.andone.memorip.presentation.screen.user.model.SettingItemUiModel
import com.andone.memorip.presentation.screen.user.model.SettingTrailing
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData

private object SettingRowItemDimen {
    val ROW_VERTICAL_PADDING = 14.dp
    val TRAILING_ICON_PADDING = 6.dp

    val MIN_HEIGHT = 40.dp
}

@Composable
fun SettingRowItem(
    item: SettingItemUiModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = item.clickable,
                onClick = item.onClick
            )
            .padding(
                horizontal = MemoripPadding.AppHorizontalPadding,
                vertical = ROW_VERTICAL_PADDING
            )
            .heightIn(min = MIN_HEIGHT),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(item.iconRes),
            contentDescription = null,
            tint = MemoripTheme.colors.onSurface
        )

        Spacer(modifier = Modifier.width(width = MemoripPadding.PaddingSmall))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MemoripTheme.typography.bodyBold16
            )

            item.subtitle?.let {
                Text(
                    text = it,
                    style = MemoripTheme.typography.bodyRegular12,
                )
            }
        }

        when (val trailing = item.trailing) {
            is SettingTrailing.Toggle -> {
                Switch(
                    checked = trailing.checked,
                    onCheckedChange = trailing.onCheckedChange
                )
            }

            is SettingTrailing.Arrow -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!trailing.justArrow) {
                        Text(
                            text = if (trailing.isAllowed) stringResource(R.string.login_permission_allowed)
                            else stringResource(R.string.login_permission_denied),
                            style = MemoripTheme.typography.bodyRegular12,
                            color = if (trailing.isAllowed) MemoripTheme.colors.primary else MemoripTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier.width(width = TRAILING_ICON_PADDING))
                    }

                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_forward),
                        contentDescription = null
                    )
                }
            }

            is SettingTrailing.Text -> {
                Text(
                    text = trailing.text,
                    style = MemoripTheme.typography.bodyBold14,
                )
            }

            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingRowItemPreview() {
    MemoripTheme {
        SettingRowItem(
            item = DummyData.permissionItems[0]
        )
    }
}