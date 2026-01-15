package com.andone.memorip.presentation.screen.placedetail.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailTopBar(
    isMine: Boolean,
    onNavigationIconClick: () -> Unit,
    onActionIconClick: () -> Unit,
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
                    contentColor = MemoripTheme.colors.black
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }
        },
        actions = {
            if (!isMine) {
                IconButton(
                    onClick = onActionIconClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MemoripTheme.colors.primaryContainer,
                        contentColor = MemoripTheme.colors.black
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_scrap),
                        contentDescription = stringResource(R.string.place_detail_action_description)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}