package com.andone.memorip.presentation.groupdetail.component

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import com.andone.memorip.presentation.component.ImageMarker
import com.andone.memorip.presentation.model.Place
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.rememberUpdatedMarkerState

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun PlaceImageMarkers(
    places: List<Place>,
    markerImages: Map<String, Bitmap>,
    onMarkerClick: (Place) -> Unit
) {
    places.forEach { place ->
        val imageUrl = place.thumbnailImage.url
        val bitmap = markerImages[imageUrl]

        if (bitmap != null) {
            MarkerComposable(
                keys = arrayOf(place.id, imageUrl),
                state = rememberUpdatedMarkerState(
                    position = LatLng(place.latitude, place.longitude)
                ),
                onClick = {
                    onMarkerClick(place)
                    true
                }
            ) {
                ImageMarker(imageBitmap = bitmap)
            }
        }
    }
}