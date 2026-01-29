package com.andone.memorip.presentation.screen.groupdetail.component

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.component.map.ClusterMarker
import com.andone.memorip.presentation.component.map.ImageMarker
import com.andone.memorip.presentation.component.map.MapClusterManager
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
    clusteredItems: List<MapClusterManager.ClusterItem>,
    markerImages: Map<String, Bitmap>,
    onMarkerClick: (Place) -> Unit,
    onClusterClick: (MapClusterManager.ClusterItem) -> Unit
) {
    // 클러스터 마커 렌더링
    clusteredItems.filter { it.isCluster }.forEach { clusterItem ->
        val clusterKey = "cluster_${clusterItem.position}_${clusterItem.count}"
        key(clusterKey) {
            MarkerComposable(
                keys = arrayOf("cluster", clusterItem.position.toString(), clusterItem.count.toString()),
                state = rememberUpdatedMarkerState(position = clusterItem.position),
                onClick = {
                    onClusterClick(clusterItem)
                    true
                }
            ) {
                ClusterMarker(count = clusterItem.count)
            }
        }
    }

    // 단일 마커 렌더링
    clusteredItems.filter { !it.isCluster }.forEach { clusterItem ->
        val placeData = clusterItem.places.firstOrNull() ?: return@forEach
        val place = placeData.placeData as? Place ?: return@forEach
        key(place.id) {
            val imageUrl = placeData.imageUrl
            val bitmap = markerImages[imageUrl]

            if (bitmap != null) {
                MarkerComposable(
                    keys = arrayOf(place.id, imageUrl),
                    state = rememberUpdatedMarkerState(position = clusterItem.position),
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

        val clusteredItems = DummyData.places.map { place ->
            MapClusterManager.ClusterItem(
                position = LatLng(place.latitude, place.longitude),
                places = listOf(
                    MapClusterManager.PlaceClusterData(
                        id = place.id,
                        position = LatLng(place.latitude, place.longitude),
                        imageUrl = place.thumbnailImage.url,
                        placeData = place
                    )
                ),
                isCluster = false
            )
        }

        PlaceImageMarkers(
            clusteredItems = clusteredItems,
            markerImages = markerImages,
            onMarkerClick = {},
            onClusterClick = {}
        )
    }
}
