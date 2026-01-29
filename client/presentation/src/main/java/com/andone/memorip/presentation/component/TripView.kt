package com.andone.memorip.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.grouplist.component.EmptyTripPlaceholder
import com.andone.memorip.presentation.screen.grouplist.component.TripLayout
import com.andone.memorip.presentation.screen.grouplist.component.ImageCard
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.buildBento5x3Items

@Composable
fun TripView(
    name: String,
    onTripClick: () -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    images: List<String> = emptyList()
) {
    Column(
        modifier = modifier.clickable(
            interactionSource = null,
            indication = null,
            onClick = onTripClick
        ),
    ) {
        Text(
            text = name.ifBlank { stringResource(R.string.trip_view_default_name) },
            modifier = Modifier.padding(all = MemoripSpace.SpaceXXSmall),
            style = MemoripTheme.typography.titleBold18
        )
        if (images.isEmpty()) {
            EmptyTripPlaceholder(onClick = onAddClick)
        } else {
            TripLayout(
                items = buildBento5x3Items(images),
                modifier = Modifier
                    .fillMaxWidth()
            ) { item ->
                ImageCard(item = item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TripViewPreview() {
    MemoripTheme {
        TripView(
            name = "기본 그룹",
            onTripClick = {},
            onAddClick = {},
            images = List(8) { "" }
        )
    }
}