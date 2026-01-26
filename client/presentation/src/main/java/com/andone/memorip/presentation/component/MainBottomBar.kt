package com.andone.memorip.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.andone.memorip.presentation.theme.MemoripHeight
import com.andone.memorip.presentation.theme.MemoripTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private object MainBottomBarDimens {
    val centerButtonSize = 52.dp
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
    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

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
            modifier = Modifier
                .fillMaxWidth()
                .height(MemoripHeight.bottomBar + bottomPadding)
                .background(MemoripTheme.colors.background),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(MemoripHeight.bottomBar),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, tab ->
                    if (index == tabs.size / 2) {
                        Spacer(modifier = Modifier.width(centerButtonSize))
                    }

                    val iconColor =
                        if (tab == currentTab) MemoripTheme.colors.primary else MemoripTheme.colors.gray

                    IconButton(
                        onClick = { onTabSelected(tab) },
                        colors = IconButtonDefaults.iconButtonColors(contentColor = iconColor)
                    ) {
                        Icon(
                            painter = painterResource(tab.selectedIconId),
                            contentDescription = stringResource(tab.titleTextId)
                        )
                    }
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