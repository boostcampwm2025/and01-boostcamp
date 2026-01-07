package com.andone.memorip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.domain.model.NetworkStatus
import com.andone.memorip.navigation.MemoripNav
import com.andone.memorip.navigation.MemoripNavigator
import com.andone.memorip.presentation.component.MainBottomBar
import com.andone.memorip.presentation.component.MemoripSnackbar
import com.andone.memorip.presentation.common.SnackBarManager
import com.andone.memorip.presentation.common.SnackBarRequest
import com.andone.memorip.presentation.component.NetworkStatusBanner
import com.andone.memorip.presentation.observer.NetworkViewModel

@Composable
fun MemoripApp(
    navigator: MemoripNavigator,
    snackbarManager: SnackBarManager,
    networkViewModel: NetworkViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val networkStatus by networkViewModel.networkStatus.collectAsStateWithLifecycle()

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
            MemoripSnackbar(hostState = snackbarHostState)
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column {
            NetworkStatusBanner(networkStatus)
            MemoripNav(
                navigator = navigator,
                innerPadding = innerPadding,
            )
        }
    }
}