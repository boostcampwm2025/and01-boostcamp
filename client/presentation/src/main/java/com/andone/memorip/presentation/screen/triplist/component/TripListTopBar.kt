package com.andone.memorip.presentation.screen.triplist.component

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

private val TopBarShadowElevation = 4.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListTopBar(
    modifier: Modifier = Modifier,
    hasShadow: Boolean = false
) {
    val topBarModifier = if (hasShadow) { modifier.shadow(elevation = TopBarShadowElevation)
    } else {
        modifier
    }
    TopAppBar(
        modifier = topBarModifier,
        title = {
            Text(
                text = stringResource(R.string.trip_list_title),
                style = MemoripTheme.typography.titleBold20,
                color = MemoripTheme.colors.onSurface
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MemoripTheme.colors.background,
            titleContentColor = MemoripTheme.colors.onSurface
        )
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TripListTopBarPreview() {
    MemoripTheme {
        TripListTopBar()
    }
}
