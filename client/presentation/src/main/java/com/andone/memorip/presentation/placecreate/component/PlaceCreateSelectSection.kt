package com.andone.memorip.presentation.placecreate.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.theme.MemoripTheme

@Composable
fun PlaceCreateSelectSection(
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onGroupClick: () -> Unit
) {
    Column {
        SelectRow(
            label = stringResource(R.string.place_create_category),
            value = "",
            leadingIcon = painterResource(R.drawable.ic_tag),
            onClick = onCategoryClick
        )
        SelectRow(
            label = stringResource(R.string.place_create_location),
            value = "",
            leadingIcon = painterResource(R.drawable.ic_location_on),
            onClick = onLocationClick
        )
        SelectRow(
            label = stringResource(R.string.place_create_group),
            value = "",
            leadingIcon = painterResource(R.drawable.ic_folder),
            onClick = onGroupClick
        )
    }
}

@Preview
@Composable
private fun PlaceCreateSelectSectionPreview(){
    MemoripTheme {
        PlaceCreateSelectSection(
            onCategoryClick = {},
            onLocationClick = {},
            onGroupClick = {}
        )
    }
}
