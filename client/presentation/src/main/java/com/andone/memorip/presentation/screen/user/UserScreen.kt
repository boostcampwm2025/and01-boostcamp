package com.andone.memorip.presentation.screen.user

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.BuildConfig
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.user.UserScreenDimen.ACCOUNT_SECTION_HEIGHT
import com.andone.memorip.presentation.screen.user.component.AccountSection
import com.andone.memorip.presentation.screen.user.component.AlarmSection
import com.andone.memorip.presentation.screen.user.component.AppInfoSection
import com.andone.memorip.presentation.screen.user.component.LoginDialog
import com.andone.memorip.presentation.screen.user.component.PermissionSection
import com.andone.memorip.presentation.screen.user.component.PolicySection
import com.andone.memorip.presentation.screen.user.component.SettingSection
import com.andone.memorip.presentation.screen.user.component.UserProfileSection
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.screen.user.model.UserEvent
import com.andone.memorip.presentation.screen.user.model.LoginMethod
import com.andone.memorip.presentation.screen.user.model.UserUiModel
import com.andone.memorip.presentation.screen.user.model.UserUiState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import com.andone.memorip.presentation.util.DummyData
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

private object UserScreenDimen {
    val ACCOUNT_SECTION_HEIGHT = 120.dp
}

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        viewModel.onAction(action = UserAction.OnLocationPermissionResult(granted))
        viewModel.onAction(action = UserAction.RefreshAuthState)
    }

    val credentialManager = remember {
        CredentialManager.create(context)
    }

    val clientId = BuildConfig.LOGIN_WEB_CLIENT_ID
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
            UserEvent.RequestGoogleLogin -> {
                try {
                    val result = credentialManager.getCredential(
                        context = context,
                        request = request
                    )

                    val credential = result.credential
                    if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(data = credential.data)
                        viewModel.onAction(action = UserAction.GoogleLoginSuccess(idToken = googleIdTokenCredential.idToken))
                    }
                } catch (e: Exception) {
                    viewModel.onAction(action = UserAction.OnMethodClick(method = LoginMethod.EMAIL))
                }
            }
        }
    }

    UserScreenContent(
        state = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
fun UserScreenContent(
    state: UserUiState,
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.showLoginDialog) {
        LoginDialog(
            state = state,
            onAction = onAction,
        )
    }

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .padding(all = MemoripPadding.AppHorizontalPadding)
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium)
        ) {
            ProfileSection(state = state, onAction = onAction)

            AlarmSection(state.alarmUiState, onAction = onAction)

            PermissionSection(state.permissionUiState, onAction = onAction)

            PolicySection(onAction = onAction)

            AppInfoSection(state.appVersion, onAction = onAction)

            if (state.isLoggedIn) {
                AccountSection(onAction = onAction)
            }
        }
    }
}

@Composable
private fun ProfileSection(
    state: UserUiState,
    onAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height = ACCOUNT_SECTION_HEIGHT)
            .clickable(
                enabled = !state.isLoggedIn,
                onClick = {
                    onAction(UserAction.OnMethodClick(method = LoginMethod.GOOGLE))
                }
            ),
        color = MemoripTheme.colors.primaryContainer,
        shape = memoripShapes.roundedSmall,
        contentColor = MemoripTheme.colors.onSurface
    ) {
        if (state.isLoggedIn) {
            UserProfileSection(
                user = state.user,
                onEditClick = {},
                onProfileImageClick = {}
            )
        } else {
            Box(contentAlignment = Alignment.Center) {
                Text(text = stringResource(R.string.login_add_account))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenContentsPreview() {
    MemoripTheme {
        UserScreenContent(
            state = UserUiState(),
            onAction = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserScreenContentsLoginPreview() {
    MemoripTheme {
        UserScreenContent(
            state = UserUiState(
                isLoggedIn = true,
                user = DummyData.dummyUser
            ),
            onAction = {},
        )
    }
}