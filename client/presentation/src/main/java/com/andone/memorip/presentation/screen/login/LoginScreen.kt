package com.andone.memorip.presentation.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun LoginScreen() {
    LoginScreenContents()
}

@Composable
fun LoginScreenContents() {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(paddingValues = innerPadding)) {

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenContentsPreview() {
    MemoripTheme {
        LoginScreenContents()
    }
}