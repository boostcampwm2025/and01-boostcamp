package com.andone.memorip.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.navigation.MainBottomBarRoute
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.MainBottomBarConstants.DURATION_MILLIS
import com.andone.memorip.presentation.component.MainBottomBarDimens.centerButtonSize
import com.andone.memorip.presentation.component.MainBottomBarDimens.elevation
import com.andone.memorip.presentation.theme.MemoripTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private object MainBottomBarDimens {
    val centerButtonSize = 56.dp
    val elevation = 8.dp
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
            Surface(
                color = MemoripTheme.colors.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .height(80.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomBarSlot(
                        modifier = Modifier.weight(1f),
                        tab = tabs.getOrNull(0),
                        currentTab = currentTab,
                        onTabSelected = onTabSelected
                    )

                    BottomBarSlot(
                        modifier = Modifier.weight(1f),
                        tab = tabs.getOrNull(1),
                        currentTab = currentTab,
                        onTabSelected = onTabSelected
                    )

                    // 중앙 버튼 spacer
                    Spacer(modifier = Modifier.weight(0.5f))

                    // todo : 시간표 탭
                    Spacer(modifier = Modifier.weight(1f))

                    BottomBarSlot(
                        modifier = Modifier.weight(1f),
                        tab = tabs.getOrNull(2),
                        currentTab = currentTab,
                        onTabSelected = onTabSelected
                    )
                }
            }
            BottomBarCenterButton(onClick = onFabClick)
        }
    }
}

@Composable
private fun BottomBarSlot(
    modifier: Modifier = Modifier,
    tab: MainBottomBarRoute?,
    currentTab: MainBottomBarRoute?,
    onTabSelected: (MainBottomBarRoute) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        tab?.let {
            BottomBarNavigationItem(
                tab = it,
                isSelected = it == currentTab,
                onTabSelected = onTabSelected
            )
        }
    }
}

@Composable
private fun BottomBarNavigationItem(
    tab: MainBottomBarRoute,
    isSelected: Boolean,
    onTabSelected: (MainBottomBarRoute) -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onTabSelected(tab) }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(tab.selectedIconId),
                contentDescription = stringResource(tab.titleTextId),
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MemoripTheme.colors.gray
                },
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(tab.titleTextId),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MemoripTheme.colors.gray
            },
            textAlign = TextAlign.Center
        )
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
            .offset(y = -centerButtonSize)
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