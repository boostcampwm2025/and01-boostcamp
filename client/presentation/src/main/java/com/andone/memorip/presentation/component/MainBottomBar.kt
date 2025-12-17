package com.andone.memorip.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.navigation.MainBottomBarRoute
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.component.MainBottomBarDimens.DURATION_MILLIS
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private object MainBottomBarDimens {
    const val DURATION_MILLIS = 500
}

@Composable
fun MainBottomBar(
    visible: Boolean,
    tabs: ImmutableList<MainBottomBarRoute>,
    currentTab: MainBottomBarRoute?,
    onTabSelected: (MainBottomBarRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = DURATION_MILLIS)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = DURATION_MILLIS)
        )
    ) {
        NavigationBar(containerColor = MemoripTheme.colors.offWhite) {
            tabs.forEach { tab ->
                NavigationBarItem(
                    selected = tab == currentTab,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Icon(
                            painter = painterResource(tab.selectedIconId),
                            contentDescription = stringResource(tab.titleTextId)
                        )
                    },
                    label = { Text(text = stringResource(tab.titleTextId)) },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = MemoripTheme.colors.primaryContainer)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    MainBottomBar(
        visible = true,
        tabs = persistentListOf(
            MainBottomBarRoute.HOME,
            MainBottomBarRoute.USER
        ),
        currentTab = MainBottomBarRoute.HOME,
        onTabSelected = {}
    )
}