package com.andone.memorip.presentation.screen.placedetail.model

import android.os.Parcelable
import com.andone.memorip.presentation.model.TagUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceUiModel(
    val id: String = "",
    val title: String = "",
    val tags: ImmutableList<TagUiModel> = persistentListOf(),
    val locationName: String = "",
    val latitude: Double = 0.toDouble(),
    val longitude: Double = 0.toDouble(),
    val imageUrls: ImmutableList<String> = persistentListOf(),
    val trips: ImmutableList<TripCompactUiModel> = persistentListOf(),
    val content: String = "",
    val isMine: Boolean = false,
    val isInMyTrip: Boolean = false,
    val isPublic: Boolean = true
) : Parcelable