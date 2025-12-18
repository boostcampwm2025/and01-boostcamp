package com.andone.memorip

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.andone.memorip.navigation.MemoripNav
import com.andone.memorip.navigation.MemoripNavigator
import com.andone.memorip.presentation.component.MainBottomBar

@Composable
fun MemoripApp(navigator: MemoripNavigator) {
    Scaffold(
        bottomBar = {
            MainBottomBar(
                visible = navigator.isShowBottomBar,
                tabs = navigator.mainBottomBars,
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigateToTab
            )
        },
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        MemoripNav(
            navigator = navigator,
            innerPadding = innerPadding,
        )
    }
}