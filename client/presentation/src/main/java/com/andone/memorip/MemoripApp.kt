package com.andone.memorip

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.andone.memorip.navigation.MemoripNav
import com.andone.memorip.navigation.MemoripNavigator
import com.andone.memorip.presentation.component.MainBottomBar
import com.andone.memorip.presentation.component.MemoripSnackbar
import com.andone.memorip.presentation.component.NetworkStatusBanner
import com.andone.memorip.presentation.observer.NetworkViewModel
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.util.collectWithLifecycle
import com.andone.memorip.presentation.util.snackbar.SnackBarManager

@Composable
fun MemoripApp(
    navigator: MemoripNavigator,
    snackbarManager: SnackBarManager,
    networkViewModel: NetworkViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val networkStatus by networkViewModel.networkStatus.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val systemNavigationBarPadding = WindowInsets.navigationBars
        .asPaddingValues()
        .calculateBottomPadding()
    val snackbarBottomPadding = MemoripHeight.bottomBar + systemNavigationBarPadding


    @SuppressLint("LocalContextGetResourceValueCall")
    snackbarManager.message.collectWithLifecycle { event ->
        val result = snackbarHostState.showSnackbar(
            message = context.getString(event.messageResId),
            duration = SnackbarDuration.Short
        )
        if (result == SnackbarResult.ActionPerformed) {
            event.action?.onAction?.invoke()
        }
    }

    Scaffold(
        bottomBar = {
            MainBottomBar(
                visible = navigator.isShowBottomBar,
                tabs = navigator.mainBottomBars,
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigateToTab,
                onFabClick = navigator::navigateToPlaceCreate
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            MemoripNav(
                navigator = navigator,
                innerPadding = innerPadding,
            )
            NetworkStatusBanner(
                status = networkStatus,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = snackbarBottomPadding)
            ) {
                MemoripSnackbar(
                    hostState = snackbarHostState,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}
