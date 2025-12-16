package com.andone.memorip.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GroupView(
    name: String,
    images: List<String>,
    modifier: Modifier = Modifier
){
    Column {
        Text(name)
    }
}