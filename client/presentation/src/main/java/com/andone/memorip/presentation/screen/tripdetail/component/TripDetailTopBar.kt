package com.andone.memorip.presentation.screen.tripdetail.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.tripdetail.component.TripDetailTopBarDimen.ELEVATION_EXPANDED
import com.andone.memorip.presentation.screen.tripdetail.component.TripDetailTopBarDimen.ELEVATION_NORMAL
import com.andone.memorip.presentation.theme.MemoripTheme

private object TripDetailTopBarDimen {
    val ELEVATION_NORMAL = 0.dp
    val ELEVATION_EXPANDED = 8.dp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isBottomSheetExpanded: Boolean = false
) {
    Surface(
        shadowElevation = if (isBottomSheetExpanded) ELEVATION_EXPANDED else ELEVATION_NORMAL,
        tonalElevation = 0.dp,
        color = MemoripTheme.colors.background,
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    style = MemoripTheme.typography.headlineBold20,
                    color = MemoripTheme.colors.onSurface
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.trip_detail_back_button_content_description)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MemoripTheme.colors.background,
                navigationIconContentColor = MemoripTheme.colors.onSurface,
                titleContentColor = MemoripTheme.colors.onSurface,
                actionIconContentColor = MemoripTheme.colors.onSurface
            )
        )
    }
}

@Preview
@Composable
private fun TripDetailTopBarPreview() {
    MemoripTheme {
        TripDetailTopBar(
            title = "Trip1",
            onBackClick = {}
        )
    }
}