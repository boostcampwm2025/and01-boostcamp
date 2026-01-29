package com.andone.memorip.presentation.screen.user.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun AccountDialog(
    onAction: (UserAction) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isLogout: Boolean = true
) {
    val titleRes = if (isLogout) R.string.login_logout else R.string.login_delete_account
    val descriptionRes = if (isLogout) R.string.login_logout_description else R.string.login_delete_account_description
    val confirmRes = if (isLogout) R.string.login_logout else R.string.login_delete_account_confirm

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = titleRes))
        },
        text = {
            Text(text = stringResource(id = descriptionRes))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isLogout) {
                        onAction(UserAction.SignOut)
                        onDismiss()
                    } else {
                        onAction(UserAction.DeleteAccount)
                        onDismiss()
                    }
                }
            ) {
                Text(
                    text = stringResource(id = confirmRes),
                    color = if (isLogout)
                        MemoripTheme.colors.primary
                    else
                        MemoripTheme.colors.error
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(id = R.string.login_cancel))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun AccountDialogPreview() {
    MemoripTheme {
        AccountDialog(
            onAction = {},
            onDismiss = {}
        )
    }
}