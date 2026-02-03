package com.andone.memorip.presentation.screen.plan.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.screen.plan.component.PlacePickerItemConstant.FULL_WEIGHT
import com.andone.memorip.presentation.screen.plan.component.PlacePickerItemDimen.PLACE_PICKER_ITEM_HEIGHT
import com.andone.memorip.presentation.screen.plan.component.PlacePickerItemDimen.PLACE_PICKER_ITEM_WIDTH
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripTheme

private object PlacePickerItemConstant {
    const val FULL_WEIGHT = 1f
}

private object PlacePickerItemDimen {
    val PLACE_PICKER_ITEM_HEIGHT = 150.dp
    val PLACE_PICKER_ITEM_WIDTH = 100.dp
}

@Composable
fun PlacePickerItem(
    place: Place,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(PLACE_PICKER_ITEM_HEIGHT)
            .width(PLACE_PICKER_ITEM_WIDTH)
            .background(
                color = MemoripTheme.colors.primaryContainer,
                shape = MemoripTheme.shapes.roundedMedium
            )
            .clip(shape = MemoripTheme.shapes.roundedMedium)
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(FULL_WEIGHT)
                .fillMaxWidth(),
            model = place.thumbnailImage.url,
            contentDescription = stringResource(R.string.place_picker_item_image_description),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(FULL_WEIGHT)
                .fillMaxWidth()
                .padding(MemoripPadding.PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(MemoripPadding.PaddingXXXSmall)
        ) {
            Text(
                text = place.name,
                style = MemoripTheme.typography.titleBold12,
                color = MemoripTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}