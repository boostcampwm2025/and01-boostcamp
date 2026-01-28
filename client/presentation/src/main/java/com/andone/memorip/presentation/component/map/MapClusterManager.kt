package com.andone.memorip.presentation.component.map

import android.util.Log
import com.naver.maps.geometry.LatLng

class MapClusterManager {
    data class ClusterItem(
        val position: LatLng,
        val places: List<PlaceClusterData>,
        val isCluster: Boolean
    ) {
        val count: Int get() = places.size
    }

    data class PlaceClusterData(
        val id: String,
        val position: LatLng,
        val imageUrl: String,
        val placeData: Any? = null
    )

    private companion object ClusteringConfig {
        const val LOG_TAG = "MapCluster"
    }

    fun cluster(
        places: List<PlaceClusterData>,
        gridSize: Double
    ): List<ClusterItem> {
        if (places.isEmpty()) return emptyList()

        logClusteringStart(gridSize, places.size)

        val gridMap = groupPlacesByGrid(places, gridSize)
        val result = convertGridToClusterItems(gridMap)

        return result
    }


    private fun getGridKey(position: LatLng, gridSize: Double): String {
        val gridX = (position.longitude / gridSize).toInt()
        val gridY = (position.latitude / gridSize).toInt()
        return "$gridX,$gridY"
    }

    private fun groupPlacesByGrid(
        places: List<PlaceClusterData>,
        gridSize: Double
    ): Map<String, List<PlaceClusterData>> {
        val gridMap = mutableMapOf<String, MutableList<PlaceClusterData>>()

        places.forEach { place ->
            val gridKey = getGridKey(place.position, gridSize)
            gridMap.getOrPut(gridKey) { mutableListOf() }.add(place)
        }

        return gridMap
    }

    private fun convertGridToClusterItems(
        gridMap: Map<String, List<PlaceClusterData>>
    ): List<ClusterItem> {
        return gridMap.values.map { cellPlaces ->
            if (cellPlaces.size == 1) {
                createSingleMarker(cellPlaces.first())
            } else {
                createCluster(cellPlaces)
            }
        }
    }

    private fun createSingleMarker(place: PlaceClusterData): ClusterItem {
        return ClusterItem(
            position = place.position,
            places = listOf(place),
            isCluster = false
        )
    }

    private fun createCluster(places: List<PlaceClusterData>): ClusterItem {
        val centerPosition = calculateCenterPosition(places)
        return ClusterItem(
            position = centerPosition,
            places = places,
            isCluster = true
        )
    }

    private fun calculateCenterPosition(places: List<PlaceClusterData>): LatLng {
        val centerLat = places.map { it.position.latitude }.average()
        val centerLng = places.map { it.position.longitude }.average()
        return LatLng(centerLat, centerLng)
    }

    private fun logClusteringStart(gridSize: Double, placesCount: Int) {
        Log.d(LOG_TAG, "Grid Size: $gridSize, Places: $placesCount")
    }
}
