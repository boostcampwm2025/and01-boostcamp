package com.andone.memorip.presentation.screen.placecreate.component

import android.content.res.Configuration
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.placecreate.model.PlaceCreateStep
import com.andone.memorip.presentation.theme.MemoripTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceCreateTopBar(
    currentStep: PlaceCreateStep,
    onBackClick: () -> Unit
) {
    val topBarText = when (currentStep) {
        PlaceCreateStep.SelectImage -> stringResource(R.string.select_image_title)
        PlaceCreateStep.SelectLocation -> stringResource(R.string.select_location_title)
        PlaceCreateStep.PlaceCreate -> stringResource(R.string.place_create_title)
        PlaceCreateStep.SelectCategory -> stringResource(R.string.select_category_title)
        PlaceCreateStep.SelectTrip -> stringResource(R.string.select_trip_title)
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = topBarText,
                style = MemoripTheme.typography.headlineBold20,
                color = MemoripTheme.colors.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.place_create_back_content_description)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MemoripTheme.colors.background)
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlaceCreateTopBarPreview() {
    MemoripTheme {
        PlaceCreateTopBar(
            currentStep = PlaceCreateStep.PlaceCreate,
            onBackClick = {},
        )
    }
}
