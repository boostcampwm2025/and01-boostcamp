package com.andone.memorip

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.navigation.MemoripNav

@Composable
fun MemoripApp() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        MemoripNav(Modifier.padding(innerPadding))
    }
}

@Preview(showBackground = true)
@Composable
fun MemoripAppPreview() {
    MemoripApp()
}