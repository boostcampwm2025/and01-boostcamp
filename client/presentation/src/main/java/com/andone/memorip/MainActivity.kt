package com.andone.memorip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.ui.theme.MemoripTheme

@Composable
fun MemoripApp() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Text(
            text = "asdf",
            modifier = Modifier
                .padding(innerPadding)
                .background(MemoripTheme.colors.offWhite)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MemoripAppPreview() {
    MemoripApp()
}