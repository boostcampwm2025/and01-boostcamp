package com.andone.memorip

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.andone.memorip.navigation.MemoripNav
import com.andone.memorip.navigation.MemoripNavigator
import com.andone.memorip.presentation.component.MainBottomBar
import com.andone.memorip.presentation.common.SnackbarManager

@Composable
fun MemoripApp(
    navigator: MemoripNavigator,
    snackbarManager: SnackbarManager
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        snackbarManager.message.collect { msg ->
            val result = snackbarHostState.showSnackbar(
                message = msg.message,
                actionLabel = msg.actionLabel,
                duration = msg.duration
            )
            if (result == SnackbarResult.ActionPerformed) {
                msg.onAction?.invoke()
            }
        }
    }

    Scaffold(
        bottomBar = {
            MainBottomBar(
                visible = navigator.isShowBottomBar,
                tabs = navigator.mainBottomBars,
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigateToTab
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        MemoripNav(
            navigator = navigator,
            innerPadding = innerPadding,
        )
    }
}