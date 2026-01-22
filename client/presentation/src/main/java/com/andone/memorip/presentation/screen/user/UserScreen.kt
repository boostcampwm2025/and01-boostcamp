package com.andone.memorip.presentation.screen.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.BuildConfig
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MemoripImage
import com.andone.memorip.presentation.screen.user.UserScreenConstants.ACCOUNT_SECTION_HEIGHT
import com.andone.memorip.presentation.screen.user.UserScreenConstants.PLACES_SECTION_HEIGHT
import com.andone.memorip.presentation.screen.user.UserScreenConstants.PROFILE_IMAGE_SIZE
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.screen.user.model.UserEvent
import com.andone.memorip.presentation.screen.user.model.LoginMethod
import com.andone.memorip.presentation.screen.user.model.UserUiModel
import com.andone.memorip.presentation.screen.user.model.UserUiState
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.theme.memoripShapes
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL

private object UserScreenConstants {
    val ACCOUNT_SECTION_HEIGHT = 180.dp
    val PLACES_SECTION_HEIGHT = 800.dp
    val PROFILE_IMAGE_SIZE = 80.dp
}

@Composable
fun UserScreen(
    modifier: Modifier = Modifier,
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
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
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .verticalScroll(state = rememberScrollState()),
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = ACCOUNT_SECTION_HEIGHT)
                    .padding(all = MemoripPadding.PaddingSmall)
                    .clickable(
                        enabled = !state.isLoggedIn,
                        onClick = {
                            onAction(UserAction.OnMethodClick(method = LoginMethod.GOOGLE))
                        }
                    ),
                color = MemoripTheme.colors.primaryContainer,
                shape = memoripShapes.roundedSmall
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (state.isLoggedIn) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier
                                    .size(PROFILE_IMAGE_SIZE)
                                    .clip(memoripShapes.roundedXLarge)
                            ) {
                                val imageUrl = state.user.profileImgUrl

                                if (imageUrl.isNotBlank()) {
                                    MemoripImage(
                                        imageUrl = imageUrl,
                                        contentDescription = stringResource(R.string.login_user_profile_image),
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(width = MemoripSpace.SpaceXXXLarge))
                            Column {
                                Text(text = state.user.id)
                                Text(text = state.user.name)
                            }
                        }
                    } else {
                        Text(text = stringResource(R.string.login_add_account))
                    }
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = PLACES_SECTION_HEIGHT)
                    .padding(all = MemoripPadding.PaddingSmall),
                color = MemoripTheme.colors.primaryContainer,
                shape = memoripShapes.roundedSmall,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.login_show_places))
                }
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
private fun UserScreenContentsLogedinPreview() {
    MemoripTheme {
        UserScreenContent(
            state = UserUiState(
                isLoggedIn = true,
                user = UserUiModel(
                    name = "홍길동",
                    id = "0",
                    profileImgUrl = ""
                )
            ),
            onAction = {},
        )
    }
}