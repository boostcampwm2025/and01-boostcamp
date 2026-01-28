package com.andone.memorip.presentation.component.map

import android.content.Context
import android.util.Log
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.Projection
import kotlin.math.sqrt

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

    private companion object ClusteringConfig {
        const val LOG_TAG = "MapCluster"
    }

    fun cluster(
        places: List<PlaceClusterData>,
        projection: Projection,
        clusterRadiusDp: Int = 60 // 보통 50~80dp가 적당합니다.
    ): List<ClusterItem> {
        if (places.isEmpty()) return emptyList()

        // DP를 현재 기기의 Pixel 단위로 변환
        val density = context.resources.displayMetrics.density
        val clusterRadiusPx = clusterRadiusDp * density

        // 내부 작업용 MutableCluster 리스트
        data class MutableCluster(
            val position: LatLng,
            val places: MutableList<PlaceClusterData>
        )

        val clusters = mutableListOf<MutableCluster>()

        for (place in places) {
            // 1. 현재 장소의 화면상 좌표(Pixel)를 구함
            val screenPos = projection.toScreenLocation(place.position)
            
            // 2. 기존 클러스터 중 가장 가까운 클러스터 찾기
            var closestCluster: MutableCluster? = null
            var minDistance = Double.MAX_VALUE

            for (cluster in clusters) {
                val clusterScreenPos = projection.toScreenLocation(cluster.position)
                val distance = calculateDistance(
                    screenPos.x.toDouble(), screenPos.y.toDouble(),
                    clusterScreenPos.x.toDouble(), clusterScreenPos.y.toDouble()
                )

                // 설정한 DP 거리 내에 있고, 가장 가까운 경우
                if (distance < clusterRadiusPx && distance < minDistance) {
                    closestCluster = cluster
                    minDistance = distance
                }
            }

            // 3. 할당 또는 신규 생성
            if (closestCluster != null) {
                closestCluster.places.add(place)
                // 클러스터의 중심점을 새로 계산하고 싶다면 여기서 position을 업데이트할 수 있습니다.
                // 보통 첫 번째 점을 기준으로 유지하거나, 평균값을 사용합니다.
            } else {
                clusters.add(
                    MutableCluster(
                        position = place.position,
                        places = mutableListOf(place)
                    )
                )
            }
        }

        // MutableCluster를 불변 ClusterItem으로 변환
        val result = clusters.map { mutableCluster ->
            ClusterItem(
                position = mutableCluster.position,
                places = mutableCluster.places.toList()
            )
        }

        logClusteringResult(result.size, places.size)
        return result
    }

    private fun calculateDistance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
        return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
    }

    private fun logClusteringResult(clusterCount: Int, totalPlaces: Int) {
        Log.d(LOG_TAG, "Clustering Done: $clusterCount clusters from $totalPlaces places")
    }
}
