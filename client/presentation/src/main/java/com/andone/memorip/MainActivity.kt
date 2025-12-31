package com.andone.memorip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.andone.memorip.navigation.rememberMemoripNavigator
import com.andone.memorip.presentation.placelist.PlaceListScreenContents
import com.andone.memorip.presentation.theme.MemoripTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoripTheme {
//                val navigator = rememberMemoripNavigator()
//                MemoripApp(navigator)
                PlaceListScreenContents(
                    places = emptyList(),
                    onAction = {},
                )
            }
        }
    }
}