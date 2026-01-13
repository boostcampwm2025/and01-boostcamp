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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.presentation.screen.groupdetail.model.GroupDetailEvent
import com.andone.memorip.presentation.screen.login.model.LoginAction
import com.andone.memorip.presentation.screen.login.model.LoginUiState
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.collectWithLifecycle

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.event.collectWithLifecycle { event ->
        when (event) {
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
            Button(onClick = {}) {
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