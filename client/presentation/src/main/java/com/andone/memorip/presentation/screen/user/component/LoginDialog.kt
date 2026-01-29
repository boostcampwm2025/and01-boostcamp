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
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.screen.user.model.UserUiState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace

@Composable
fun LoginDialog(
    state: UserUiState,
    onDismiss: () -> Unit,
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPasswordMismatch =
        state.isNewAccount &&
                state.passwordConfirm.isNotEmpty() &&
                state.password != state.passwordConfirm

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        modifier = modifier,
        title = {
            Text(
                text = if (state.isNewAccount) "회원가입" else "로그인",
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
                    label = { Text("이메일") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = {
                        onAction(UserAction.UpdatePassword(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("비밀번호") },
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
                        label = { Text("비밀번호 확인") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isPasswordMismatch
                    )

                    if (isPasswordMismatch) {
                        Text(
                            text = "비밀번호가 일치하지 않습니다",
                            color = MemoripTheme.colors.error,
                            style = MemoripTheme.typography.bodyBold12
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

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
                        text = if (state.isNewAccount) "회원가입" else "이메일로 로그인"
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
                            "이미 계정이 있나요? 로그인"
                        else
                            "계정이 없나요? 회원가입",
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
            onDismiss = {},
            onAction = {}
        )
    }
}