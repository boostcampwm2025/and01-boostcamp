package com.andone.memorip.presentation.screen.groupdetail.component

import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.andone.memorip.presentation.component.map.InteractiveMultiMarkerMapView
import com.andone.memorip.presentation.model.Place
import com.andone.memorip.presentation.theme.MemoripTheme
import com.andone.memorip.presentation.util.DummyData
import com.naver.maps.geometry.LatLng

@Composable
fun MapTab(
    mapLoaded: Boolean,
    places: List<Place>,
    markerImages: Map<String, Bitmap>,
    onPlaceClick: (String) -> Unit,
    onMapLoaded: () -> Unit,
    modifier: Modifier = Modifier
) {
    InteractiveMultiMarkerMapView(
        initialBounds = places.map { LatLng(it.latitude, it.longitude) },
        onMapLoaded = onMapLoaded,
        modifier = modifier
    ) {
        if (mapLoaded) {
            PlaceImageMarkers(
                places = places,
                markerImages = markerImages,
                onMarkerClick = { place -> onPlaceClick(place.id) }
            )
        }
    }
}

@Preview(name = "지도탭 프리뷰", showBackground = true)
@Composable
private fun MapTabPreview() {
    MemoripTheme {
        MapTab(
            places = DummyData.places,
            markerImages = emptyMap(),
            onPlaceClick = {},
            mapLoaded = true,
            onMapLoaded = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}