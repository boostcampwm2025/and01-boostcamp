package com.andone.memorip.presentation.component.map

import android.content.Context
import com.andone.memorip.presentation.component.map.ClusterContant.CLUSTER_RADIUS_DP
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.Projection
import kotlin.math.sqrt


private object ClusterContant {
    const val CLUSTER_RADIUS_DP = 70
}

class MapClusterManager(private val context: Context) {

    data class ClusterItem(
        val position: LatLng,
        val places: List<PlaceClusterData>,
        val isCluster: Boolean = places.size > 1
    ) {
        val count: Int get() = places.size
    }

    data class PlaceClusterData(
        val id: String,
        val position: LatLng,
        val imageUrl: String,
        val placeData: Any? = null
    )

    fun cluster(
        places: List<PlaceClusterData>,
        projection: Projection,
        clusterRadiusDp: Int = CLUSTER_RADIUS_DP
    ): List<ClusterItem> {
        if (places.isEmpty()) return emptyList()

        // DP를 현재 기기의 Pixel 단위로 변환
        val density = context.resources.displayMetrics.density
        val clusterRadiusPx = clusterRadiusDp * density

        data class MutableCluster(
            val position: LatLng,
            val places: MutableList<PlaceClusterData>
        )

        val clusters = mutableListOf<MutableCluster>()

        for (place in places) {
            val screenPosition = projection.toScreenLocation(place.position)

            var closestCluster: MutableCluster? = null
            var minDistance = Double.MAX_VALUE

            for (cluster in clusters) {
                val clusterScreenPosition = projection.toScreenLocation(cluster.position)
                val distance = calculateDistance(
                    x1 = screenPosition.x.toDouble(),
                    y1 = screenPosition.y.toDouble(),
                    x2 = clusterScreenPosition.x.toDouble(),
                    y2 = clusterScreenPosition.y.toDouble()
                )

                if (distance < clusterRadiusPx && distance < minDistance) {
                    closestCluster = cluster
                    minDistance = distance
                }
            }

            if (closestCluster != null) {
                closestCluster.places.add(place)
            } else {
                clusters.add(
                    MutableCluster(
                        position = place.position,
                        places = mutableListOf(place)
                    )
                )
            }
        }

        val result = clusters.map { mutableCluster ->
            ClusterItem(
                position = mutableCluster.position,
                places = mutableCluster.places.toList()
            )
        }
        return result
    }

    private fun calculateDistance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
        return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }
}
