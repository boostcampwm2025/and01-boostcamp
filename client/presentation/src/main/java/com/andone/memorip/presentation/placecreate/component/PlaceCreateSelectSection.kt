package com.andone.memorip.presentation.placecreate.component

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.model.Category
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.theme.MemoripTheme
import com.naver.maps.map.compose.ExperimentalNaverMapApi

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun PlaceCreateSelectSection(
    category: List<Category>,
    location: LocationUiModel?,
    group: GroupUiModel?,
    onCategoryClick: () -> Unit,
    onLocationClick: () -> Unit,
    onGroupClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryValue =
        category.joinToString(stringResource(R.string.place_create_join_to_string_comma)) { it.category }
    val locationValue =
        location?.name?.ifEmpty { stringResource(R.string.place_create_location_placeholder) }
    val groupValue = group?.name

    Column(modifier = modifier) {
        SelectRow(
            label = stringResource(R.string.place_create_location),
            value = locationValue,
            leadingIcon = painterResource(R.drawable.ic_location_on),
            onClick = onLocationClick
        )
        LocationMapPreview(
            location = location,
            onLocationClick = onLocationClick,
            modifier = Modifier.weight(1f)
        )
        SelectRow(
            label = stringResource(R.string.place_create_group),
            value = groupValue,
            leadingIcon = painterResource(R.drawable.ic_folder),
            onClick = onGroupClick
        )
        SelectRow(
            label = stringResource(R.string.place_create_tag),
            value = categoryValue,
            leadingIcon = painterResource(R.drawable.ic_tag),
            onClick = onCategoryClick
        )
    }
}

@Preview
@Composable
private fun PlaceCreateSelectSectionPreview() {
    MemoripTheme {
        PlaceCreateSelectSection(
            category = emptyList(),
            location = null,
            group = GroupUiModel(
                name = "",
                images = emptyList()
            ),
            onCategoryClick = {},
            onLocationClick = {},
            onGroupClick = {},
        )
    }
}
