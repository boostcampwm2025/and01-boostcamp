package com.andone.memorip.presentation.placecreate.model

import android.net.Uri
import com.andone.memorip.presentation.grouplist.model.GroupUiModel
import com.andone.memorip.presentation.model.LocationUiModel
import com.andone.memorip.presentation.model.TagUiModel

data class PlaceCreateUiState(
    var title: String = "",
    var content: String = "",
    val images: List<Uri> = emptyList(),
    val category: List<TagUiModel> = emptyList(),
    val location: LocationUiModel? = null,
    val group: GroupUiModel? = null,
    val isPublic: Boolean = false,
    val isLoading: Boolean = true
)