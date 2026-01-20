package com.andone.memorip.presentation.screen.groupdetail.component

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.map.ImageMarker
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
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
        key(place.id) {
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
}

@Preview(name = "PlaceImageMarkers", showBackground = true)
@Composable
private fun PlaceImageMarkersPreview() {
    MemoripTheme {
        val context = LocalContext.current
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground)
        val bitmap = drawable?.toBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val markerImages = if (bitmap != null) {
            DummyData.places.associate { it.thumbnailImage.url to bitmap }
        } else {
            emptyMap()
        }

        PlaceImageMarkers(
            places = DummyData.places,
            markerImages = markerImages,
            onMarkerClick = {}
        )
    }
}