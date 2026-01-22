package com.andone.memorip.presentation.screen.placecreate.model

import android.net.Uri
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel
import com.andone.memorip.presentation.screen.grouplist.model.GroupUiModel

data class PlaceCreateUiState(
    var title: String = "",
    var content: String = "",
    val images: List<Uri> = emptyList(),
    val selectedImage: Uri? = null,
    val category: List<TagUiModel> = emptyList(),
    val location: LocationUiModel? = null,
    val group: GroupUiModel? = null,
    val scrollPosition: Int = 0,
    val isPublic: Boolean = false,
    val isLoading: Boolean = false
)