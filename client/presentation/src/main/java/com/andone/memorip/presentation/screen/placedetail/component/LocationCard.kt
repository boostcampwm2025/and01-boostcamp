package com.andone.memorip.presentation.placedetail.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.placedetail.component.Dimens.LocationCardHeight
import com.andone.memorip.presentation.theme.MemoripBorderWidth
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData.place
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

private object Dimens {
    val LocationCardHeight = 260.dp
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun LocationCard(
    location: String,
    title: String,
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition(LatLng(latitude, longitude), 17.0)
    }
    val markerState = remember { MarkerState(position = LatLng(latitude, longitude)) }

    Card(
        modifier = modifier,
        shape = MemoripTheme.shapes.roundedMedium,
        border = BorderStroke(
            width = MemoripBorderWidth.Thin,
            color = MemoripTheme.colors.gray
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MemoripTheme.colors.white)
                .padding(
                    horizontal = MemoripPadding.PaddingXLarge,
                    vertical = MemoripPadding.PaddingMedium
                ),
            horizontalArrangement = Arrangement.spacedBy(space = MemoripSpace.SpaceMedium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_location_on),
                tint = MemoripTheme.colors.green,
                contentDescription = null
            )
            Text(
                text = place.locationName,
                style = MemoripTheme.typography.label1,
                color = MemoripTheme.colors.black
            )
        }
        NaverMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocationCardHeight),
            cameraPositionState = cameraPosition,
        ) {
            Marker(state = markerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationCardPrev() {
    MemoripTheme {
        val place = place
        LocationCard(
            location = place.locationName,
            title = place.title,
            latitude = place.latitude,
            longitude = place.longitude,
        )
    }
}