package com.andone.memorip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.andone.memorip.navigation.rememberMemoripNavigator
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.common.SnackbarManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var snackbarManager: SnackbarManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoripTheme {
                val navigator = rememberMemoripNavigator()
                MemoripApp(
                    navigator = navigator,
                    snackbarManager = snackbarManager
                )
            }
        }
    }
}