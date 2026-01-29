package com.andone.memorip.presentation.screen.placeedit.model

import android.net.Uri
import com.andone.memorip.presentation.model.GroupUiModel
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel

data class PlaceEditUiState(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val images: List<Uri> = emptyList(),
    val selectedImage: Uri? = null,
    val location: LocationUiModel? = null,
    val groups: List<GroupUiModel> = emptyList(),
    val tags: List<TagUiModel> = emptyList(),
    val scrollPosition: Int = 0,
    val isPublic: Boolean = false,
    val isLoading: Boolean = true
)