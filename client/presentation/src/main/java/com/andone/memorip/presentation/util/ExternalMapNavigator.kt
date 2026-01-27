package com.andone.memorip.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.andone.memorip.presentation.R
import com.andone.memorip.presentation.model.LocationUiModel
import androidx.core.net.toUri

private const val NAVER_MAP_PACKAGE = "com.nhn.android.nmap"

fun Context.openMapOrAskApp(
    location: LocationUiModel,
) {
    val geoIntent = createGeoIntent(location)
    val chooserTitle = getString(R.string.place_detail_chooser_title)

    val naverMapIntent = Intent(geoIntent).apply {
        setPackage(NAVER_MAP_PACKAGE)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    val naverExists = naverMapIntent.resolveActivity(packageManager) != null

    val chooserIntent = Intent.createChooser(geoIntent, chooserTitle).apply {
        if (naverExists) {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(naverMapIntent))
        }
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(chooserIntent)
}

private fun createGeoIntent(location: LocationUiModel): Intent {
    val displayName = location.name.ifBlank { location.address }
    val geoUri: Uri =
        "geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}(${displayName})"
            .toUri()
    return Intent(Intent.ACTION_VIEW, geoUri)
}
