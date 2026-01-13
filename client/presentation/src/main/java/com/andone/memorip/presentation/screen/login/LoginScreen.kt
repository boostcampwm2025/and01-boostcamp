package com.andone.memorip.presentation.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.login.model.LoginAction
import com.andone.memorip.presentation.screen.login.model.LoginEvent
import com.andone.memorip.presentation.screen.login.model.LoginMethod
import com.andone.memorip.presentation.screen.login.model.LoginUiState
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val credentialManager = remember {
        CredentialManager.create(context)
    }

    val clientId = stringResource(R.string.login_default_web_client_id)

    val googleIdOption = remember {
        GetGoogleIdOption.Builder()
            .setServerClientId(clientId)
            .setFilterByAuthorizedAccounts(false)
            .build()
    }

    val request = remember {
        GetCredentialRequest.Builder()
            .addCredentialOption(credentialOption = googleIdOption)
            .build()
    }

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
            LoginEvent.RequestGoogleLogin -> {
                try {
                    val result = credentialManager.getCredential(
                        context = context,
                        request = request
                    )

                    val credential = result.credential
                    if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(data = credential.data)
                        viewModel.onAction(action = LoginAction.GoogleLoginSuccess(idToken = googleIdTokenCredential.idToken))
                    }
                } catch (e: Exception) {
                    android.util.Log.d("로그인 오류", "$e")
                }
            }
        }
    }

    LoginScreenContents(
        state = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
fun LoginScreenContents(
    state: LoginUiState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = { onAction(LoginAction.OnMethodClick(method = LoginMethod.GOOGLE)) }) {
                Text(text = "구글")
            }

            Button(onClick = {}) {
                Text(text = "이메일")
            }

            Button(onClick = {}) {
                Text(text = "번호")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenContentsPreview() {
    MemoripTheme {
        LoginScreenContents(
            state = LoginUiState(),
            onAction = {},
        )
    }
}