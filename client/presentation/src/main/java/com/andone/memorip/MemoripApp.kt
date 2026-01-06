package com.andone.memorip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.andone.memorip.navigation.MemoripNav
import com.andone.memorip.navigation.MemoripNavigator
import com.andone.memorip.presentation.component.MainBottomBar
import com.andone.memorip.presentation.common.SnackBarManager
import com.andone.memorip.presentation.snackbartest.SnackbarTestScreen
import com.andone.memorip.presentation.theme.MemoripTheme

private object SnackbarDimen {
    val MAX_WIDTH = 400.dp
    val MIN_HEIGHT = 80.dp
    val HORIZONTAL_PADDING = 12.dp
}

@Composable
fun MemoripApp(
    navigator: MemoripNavigator,
    snackbarManager: SnackBarManager
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
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SnackbarDimen.HORIZONTAL_PADDING),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                    Snackbar(
                        snackbarData = snackbarData,
                        modifier = Modifier
                            .widthIn(max = SnackbarDimen.MAX_WIDTH)
                            .heightIn(min = SnackbarDimen.MIN_HEIGHT),
                        containerColor = MemoripTheme.colors.gray,
                        contentColor = MemoripTheme.colors.white,
                        actionColor = MemoripTheme.colors.primary,
                        shape = MemoripTheme.shapes.roundedMedium
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        MemoripNav(
            navigator = navigator,
            innerPadding = innerPadding,
        )
    }
}