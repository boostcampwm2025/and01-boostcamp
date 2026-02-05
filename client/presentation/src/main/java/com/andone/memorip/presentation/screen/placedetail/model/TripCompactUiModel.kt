package com.andone.memorip.presentation.screen.placedetail.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TripCompactUiModel(
    val tripId: String,
    val tripName: String
) : Parcelable
