package com.andone.memorip.presentation.util

import com.andone.memorip.presentation.model.Place
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds

fun calculateBounds(places: List<Place>): LatLngBounds {
    val minLat = places.minOf { it.latitude }
    val maxLat = places.maxOf { it.latitude }
    val minLng = places.minOf { it.longitude }
    val maxLng = places.maxOf { it.longitude }

    return LatLngBounds(
        LatLng(minLat, minLng),
        LatLng(maxLat, maxLng)
    )
}