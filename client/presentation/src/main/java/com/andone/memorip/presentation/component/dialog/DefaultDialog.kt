package com.andone.memorip.presentation.component.dialog

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
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
import com.andone.memorip.presentation.theme.MemoripTheme

private object DialogDimens {
    val INNER_PADDING = 24.dp
    val SPACING = 8.dp
}

@Composable
internal fun DefaultDialog(
    title: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    confirmEnabled: Boolean = true,
    confirmButtonText: String = stringResource(R.string.dialog_confirm_message),
    cancelButtonText: String = stringResource(R.string.dialog_cancel_message),
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier,
            shape = MemoripTheme.shapes.roundedXLarge,
            color = MemoripTheme.colors.primaryContainer,
            contentColor = MemoripTheme.colors.onSurface
        ) {
            Column(
                modifier = Modifier.padding(horizontal = INNER_PADDING),
                verticalArrangement = Arrangement.spacedBy(space = SPACING),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(vertical = INNER_PADDING),
                    color = MemoripTheme.colors.onSurface,
                    style = MemoripTheme.typography.headlineBold20
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
                            text = cancelButtonText,
                            color = MemoripTheme.colors.onSurface,
                            style = MemoripTheme.typography.bodyBold16
                        )
                    }
                    TextButton(
                        onClick = onConfirmClick,
                        enabled = confirmEnabled,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MemoripTheme.colors.primary,
                            disabledContentColor = MemoripTheme.colors.lightGray
                        )
                    ) {
                        Text(
                            text = confirmButtonText,
                            style = MemoripTheme.typography.bodyBold16
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DefaultDialogPreview() {
    MemoripTheme {
        DefaultDialog(
            title = "앱 확인 알림",
            onConfirmClick = {},
            onCancelClick = {},
            onDismissRequest = {}
        ) {
            Text(
                text = "테스트하고자 생성한 Preview 입니다.\n어쩌고 저쩌고",
                style = MemoripTheme.typography.bodyMedium16,
                textAlign = TextAlign.Center
            )
        }
    }
}