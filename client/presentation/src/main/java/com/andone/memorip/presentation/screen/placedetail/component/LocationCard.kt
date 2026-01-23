package com.andone.memorip.presentation.screen.placedetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.map.ReadOnlyMapView
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.screen.placedetail.component.Dimens.LOCATION_CARD_RATIO
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData.place
import com.naver.maps.map.compose.ExperimentalNaverMapApi

private object Dimens {
    const val LOCATION_CARD_RATIO = 3f
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun LocationCard(
    location: String,
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MemoripTheme.shapes.roundedMedium,
        contentColor = MemoripTheme.colors.black,
        color = MemoripTheme.colors.primaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MemoripPadding.PaddingXLarge,
                    vertical = MemoripPadding.PaddingMedium
                ),
            horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_location_on),
                tint = MemoripTheme.colors.primary,
                contentDescription = null
            )
            Text(
                text = location,
                style = MemoripTheme.typography.label1,
                color = MemoripTheme.colors.black
            )
        }

        ReadOnlyMapView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = LOCATION_CARD_RATIO),
            location = LocationUiModel(
                address = location,
                latitude = latitude,
                longitude = longitude
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationCardPrev() {
    MemoripTheme {
        val place = place
        LocationCard(
            location = place.locationName,
            latitude = place.latitude,
            longitude = place.longitude
        )
    }
}