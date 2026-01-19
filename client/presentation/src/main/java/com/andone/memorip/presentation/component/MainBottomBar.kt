package com.andone.memorip.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.navigation.MainBottomBarRoute
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MainBottomBarConstants.DURATION_MILLIS
import com.andone.memorip.presentation.component.MainBottomBarDimens.buttonOffset
import com.andone.memorip.presentation.component.MainBottomBarDimens.centerButtonSize
import com.andone.memorip.presentation.component.MainBottomBarDimens.elevation
import com.andone.memorip.presentation.theme.MemoripTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private object MainBottomBarDimens {
    val centerButtonSize = 58.dp
    val elevation = 8.dp
    val buttonOffset = 16.dp
}

private object MainBottomBarConstants {
    const val DURATION_MILLIS = 500
}

@Composable
fun MainBottomBar(
    visible: Boolean,
    tabs: ImmutableList<MainBottomBarRoute>,
    currentTab: MainBottomBarRoute?,
    onTabSelected: (MainBottomBarRoute) -> Unit,
    onFabClick: () -> Unit,
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
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            NavigationBar(containerColor = MemoripTheme.colors.background) {
                tabs.forEachIndexed { index, tab ->
                    if (index == tabs.size / 2) {
                        Spacer(modifier = Modifier.width(width = centerButtonSize))
                    }

                    NavigationBarItem(
                        selected = tab == currentTab,
                        onClick = { onTabSelected(tab) },
                        icon = {
                            Icon(
                                painter = painterResource(tab.selectedIconId),
                                contentDescription = stringResource(tab.titleTextId)
                            )
                        },
//                        label = { Text(text = stringResource(tab.titleTextId)) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MemoripTheme.colors.onSurface,
                            selectedTextColor = MemoripTheme.colors.onSurface,
                            indicatorColor = MemoripTheme.colors.primaryContainer,
                            unselectedIconColor = MemoripTheme.colors.gray,
                            unselectedTextColor = MemoripTheme.colors.gray
                        ),
                    )
                }
            }

            BottomBarCenterButton(onClick = onFabClick)
        }
    }
}

@Composable
private fun BottomBarCenterButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .navigationBarsPadding()
            .offset(y = -buttonOffset)
            .size(centerButtonSize)
            .shadow(elevation = elevation, shape = CircleShape),
        colors = IconButtonDefaults.iconButtonColors(containerColor = MemoripTheme.colors.primary)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = stringResource(R.string.main_bottom_center_button_content_description)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    MemoripTheme {
        MainBottomBar(
            visible = true,
            tabs = MainBottomBarRoute.entries.toImmutableList(),
            currentTab = MainBottomBarRoute.PLACE_LIST,
            onTabSelected = {},
            onFabClick = {}
        )
    }
}