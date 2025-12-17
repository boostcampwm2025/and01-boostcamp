package com.andone.memorip.presentation.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.dialog.DialogDimens.INNER_PADDING
import com.andone.memorip.presentation.component.dialog.DialogDimens.SPACING
import com.andone.memorip.presentation.theme.LocalMemoripShapes
import com.andone.memorip.presentation.theme.MemoripTheme

object DialogDimens {
    val INNER_PADDING = 24.dp
    val SPACING = 8.dp
    val LEADING_ICON_SIZE = 36.dp
}

@Composable
internal fun DefaultDialog(
    title: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier,
            contentColor = MemoripTheme.colors.black,
            color = MemoripTheme.colors.primaryContainer,
            shape = MemoripTheme.shapes.largeCorner
        ) {
            Column(
                modifier = Modifier.padding(horizontal = INNER_PADDING),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = SPACING)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = INNER_PADDING),
                    text = title,
                    style = MemoripTheme.typography.headline2
                )
                content()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = INNER_PADDING),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = SPACING,
                        alignment = Alignment.End
                    )
                ) {
                    TextButton(onClick = onCancelClick) {
                        Text(
                            text = stringResource(R.string.dialog_cancel_message),
                            style = MemoripTheme.typography.label1,
                            color = MemoripTheme.colors.primary
                        )
                    }
                    TextButton(onClick = onConfirmClick) {
                        Text(
                            text = stringResource(R.string.dialog_confirm_message),
                            style = MemoripTheme.typography.label1,
                            color = MemoripTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultDialogPrev() {
    MemoripTheme(darkTheme = false) {
        DefaultDialog(
            title = "앱 확인 알림",
            onConfirmClick = {},
            onCancelClick = {},
            onDismissRequest = {}
        ) {
            Text(
                text = "테스트하고자 생성한 Preview 입니다.\n어쩌고 저쩌고",
                style = MemoripTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
    }
}