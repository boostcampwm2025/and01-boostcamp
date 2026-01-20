package com.andone.memorip.presentation.screen.placedetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.screen.placedetail.component.Dimens.LocationCardRatio
import com.andone.memorip.presentation.screen.placedetail.component.Dimens.defaultZoomLevel
import com.andone.memorip.presentation.theme.MemoripElevation
import com.andone.memorip.presentation.theme.MemoripPadding
import com.andone.memorip.presentation.theme.MemoripSpace
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData.place
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.Marker
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState

private object Dimens {
    val LocationCardRatio = 3f
    val defaultZoomLevel = 17.0
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun LocationCard(
    location: String,
    latitude: Double,
    longitude: Double,
    showMap: Boolean,
    modifier: Modifier = Modifier
) {
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition(LatLng(latitude, longitude), defaultZoomLevel)
    }
    val markerState = remember { MarkerState(position = LatLng(latitude, longitude)) }

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
                tint = MemoripTheme.colors.green,
                contentDescription = null
            )
            Text(
                text = location,
                style = MemoripTheme.typography.label1,
                color = MemoripTheme.colors.black
            )
        }

        if (showMap) {
            NaverMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio = LocationCardRatio),
                cameraPositionState = cameraPosition,
                uiSettings = MapUiSettings(
                    isCompassEnabled = false,
                    isScaleBarEnabled = false,
                    isLogoClickEnabled = false,
                    isLocationButtonEnabled = false,
                    isIndoorLevelPickerEnabled = false,
                    isZoomControlEnabled = false
                )
            ) {
                Marker(state = markerState)
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(ratio = LocationCardRatio)
            ) {}
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
            latitude = place.latitude,
            longitude = place.longitude,
            showMap = true,
        )
    }
}