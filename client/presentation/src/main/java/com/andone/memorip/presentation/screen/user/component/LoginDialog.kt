package com.andone.memorip.presentation.screen.user.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.screen.user.model.UserUiState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.R

@Composable
fun LoginDialog(
    state: UserUiState,
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPasswordMismatch =
        state.isNewAccount &&
                state.passwordConfirm.isNotEmpty() &&
                state.password != state.passwordConfirm

    AlertDialog(
        onDismissRequest = { onAction(UserAction.CloseLoginDialog) },
        confirmButton = {},
        modifier = modifier,
        containerColor = MemoripTheme.colors.background,
        title = {
            Text(
                text = if (state.isNewAccount) stringResource(R.string.login_new_account)
                else stringResource(R.string.login_login),
                style = MemoripTheme.typography.titleBold18
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MemoripSpace.SpaceSmall),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.email,
                    onValueChange = {
                        onAction(UserAction.UpdateEmail(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.login_email)) },
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = {
                        onAction(UserAction.UpdatePassword(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.login_password)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                if (state.isNewAccount) {
                    OutlinedTextField(
                        value = state.passwordConfirm,
                        onValueChange = {
                            onAction(UserAction.UpdatePasswordConfirm(it))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = stringResource(R.string.login_password_confirm)) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isPasswordMismatch
                    )

                    if (isPasswordMismatch) {
                        Text(
                            text = stringResource(R.string.login_password_not_match),
                            color = MemoripTheme.colors.error,
                            style = MemoripTheme.typography.bodyBold12
                        )
                    }
                }

                Spacer(modifier = Modifier.height(height = MemoripSpace.SpaceXSmall))

                Button(
                    onClick = {
                        onAction(UserAction.EmailLoginSubmit(state.email, state.password))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.email.isNotBlank()
                            && state.password.isNotBlank()
                            && (!state.isNewAccount || !isPasswordMismatch)
                ) {
                    Text(
                        text = if (state.isNewAccount) stringResource(R.string.login_new_account)
                        else stringResource(R.string.login_email_login)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MemoripPadding.PaddingSmall),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (state.isNewAccount)
                            stringResource(R.string.login_email_login_description)
                        else
                            stringResource(R.string.login_new_account_description),
                        style = MemoripTheme.typography.bodyMedium14,
                        modifier = Modifier.clickable {
                            onAction(UserAction.ToggleLoginMode)
                        }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun LoginDialogPreview() {
    MemoripTheme {
        LoginDialog(
            state = UserUiState(),
            onAction = {}
        )
    }
}