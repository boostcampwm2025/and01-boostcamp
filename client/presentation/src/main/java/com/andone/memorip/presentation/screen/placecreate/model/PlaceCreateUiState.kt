package com.andone.memorip.presentation.screen.placecreate.model

import android.net.Uri
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.model.TripUiModel

data class PlaceCreateUiState(
    var title: String = "",
    var content: String = "",
    val images: List<Uri> = emptyList(),
    val thumbnailImageRatio: Float = 1f,
    val selectedImage: Uri? = null,
    val category: List<TagUiModel> = emptyList(),
    val location: LocationUiModel? = null,
    val trips: List<TripUiModel> = emptyList(),
    val scrollPosition: Int = 0,
    val isPublic: Boolean = false,
    val isLoading: Boolean = false,
    val contentErrorLabel: String? = null,
)