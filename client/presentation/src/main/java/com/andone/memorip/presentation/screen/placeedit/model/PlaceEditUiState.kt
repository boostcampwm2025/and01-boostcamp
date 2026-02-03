package com.andone.memorip.presentation.screen.placeedit.model

import android.net.Uri
import androidx.core.net.toUri
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.model.TripUiModel
import com.andone.memorip.presentation.model.toUiModel
import com.andone.memorip.presentation.screen.placedetail.model.PlaceUiModel

data class PlaceEditUiState(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val images: List<Uri> = emptyList(),
    val selectedImage: Uri? = null,
    val location: LocationUiModel? = null,
    val trips: List<TripUiModel> = emptyList(),
    val tags: List<TagUiModel> = emptyList(),
    val scrollPosition: Int = 0,
    val isPublic: Boolean = false,
    val isLoading: Boolean = true
)

fun PlaceUiModel.toUiState(): PlaceEditUiState {
    return PlaceEditUiState(
        id = id,
        title = title,
        content = content,
        images = imageUrls.map { it.toUri() },
        selectedImage = null,
        location = LocationUiModel(
            name = locationName,
            address = locationName,
            roadAddress = locationName,
            latitude = latitude,
            longitude = longitude
        ),
        trips = trips.map { it.toUiModel() },
        tags = tags,
        scrollPosition = 0,
        isPublic = isPublic,
        isLoading = false
    )
}