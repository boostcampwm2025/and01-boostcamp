package com.andone.memorip.presentation.placedetail.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailTopBar(
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {},
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MemoripTheme.colors.offWhite)
    )
}