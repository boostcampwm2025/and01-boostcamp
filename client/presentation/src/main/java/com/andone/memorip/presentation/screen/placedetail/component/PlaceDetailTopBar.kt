package com.andone.memorip.presentation.screen.placedetail.component

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailTopBar(
    isMine: Boolean,
    showMoreMenu: Boolean,
    onNavigationIconClick: () -> Unit,
    onActionIconClick: () -> Unit,
    onMoreClick: () -> Unit,
    onMoreMenuDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {},
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onNavigationIconClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MemoripTheme.colors.primaryContainer,
                    contentColor = MemoripTheme.colors.onSurface
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }
        },
        actions = {
            PlaceDetailActions(
                isMine = isMine,
                showMoreMenu = showMoreMenu,
                onMoreClick = onMoreClick,
                onMoreMenuDismiss = onMoreMenuDismiss,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onActionIconClick = onActionIconClick
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
private fun PlaceDetailActions(
    isMine: Boolean,
    showMoreMenu: Boolean,
    onMoreClick: () -> Unit,
    onMoreMenuDismiss: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onActionIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isMine) {
        IconButton(
            onClick = onMoreClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MemoripTheme.colors.primaryContainer,
                contentColor = MemoripTheme.colors.onSurface
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_more_vert),
                contentDescription = stringResource(R.string.place_detail_more_menu_content_description)
            )
        }
        DropdownMenu(
            expanded = showMoreMenu,
            onDismissRequest = onMoreMenuDismiss,
            modifier = Modifier.background(MemoripTheme.colors.primaryContainer)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.place_detail_edit_menu_item),
                        color = MemoripTheme.colors.onSurface
                    )
                },
                onClick = {
                    onMoreMenuDismiss()
                    onEditClick()
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(R.string.place_detail_delete_menu_item),
                        color = MemoripTheme.colors.onSurface
                    )
                },
                onClick = {
                    onMoreMenuDismiss()
                    onDeleteClick()
                }
            )
        }
    } else {
        IconButton(
            onClick = onActionIconClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MemoripTheme.colors.primaryContainer,
                contentColor = MemoripTheme.colors.onSurface
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_scrap),
                contentDescription = stringResource(R.string.place_detail_action_description)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailTopBarPreview() {
    MemoripTheme {
        PlaceDetailTopBar(
            isMine = false,
            showMoreMenu = false,
            onNavigationIconClick = {},
            onActionIconClick = {},
            onMoreClick = {},
            onMoreMenuDismiss = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailTopBarisMinePreview() {
    MemoripTheme {
        PlaceDetailTopBar(
            isMine = true,
            showMoreMenu = true,
            onNavigationIconClick = {},
            onActionIconClick = {},
            onMoreClick = {},
            onMoreMenuDismiss = {},
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
