package com.andone.memorip.presentation.screen.placecreate.model

import android.net.Uri
import com.andone.memorip.presentation.model.GroupUiModel
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel

data class PlaceCreateUiState(
    var title: String = "",
    var content: String = "",
    val images: List<Uri> = emptyList(),
    val thumbnailImageRatio: Float = 1f,
    val selectedImage: Uri? = null,
    val category: List<TagUiModel> = emptyList(),
    val location: LocationUiModel? = null,
    val groups: List<GroupUiModel> = emptyList(),
    val scrollPosition: Int = 0,
    val isPublic: Boolean = false,
    val isLoading: Boolean = false
)