package com.andone.memorip.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.model.LocationUiModel
import androidx.core.net.toUri

private const val NAVER_MAP_PACKAGE = "com.nhn.android.nmap"

data class MapAppOption(
    val name: String,
    val packageName: String,
    val intent: Intent,
)

fun Context.openMapOrAskApp(
    location: LocationUiModel,
) {
    val geoIntent = createGeoIntent(location)

    val naverMapIntent = Intent(geoIntent).apply {
        setPackage(NAVER_MAP_PACKAGE)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        startActivity(naverMapIntent)
    } catch (e: Exception) {
        val chooserTitle = getString(R.string.place_detail_chooser_title)
        val chooserIntent = Intent.createChooser(geoIntent, chooserTitle).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(chooserIntent)
    }
}

private fun createGeoIntent(location: LocationUiModel): Intent {
    val geoUri: Uri =
        "geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}(${location.address})"
            .toUri()
    return Intent(Intent.ACTION_VIEW, geoUri)
}
