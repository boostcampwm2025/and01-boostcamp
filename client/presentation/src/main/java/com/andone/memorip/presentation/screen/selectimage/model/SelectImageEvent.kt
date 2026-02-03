package com.andone.memorip.presentation.screen.selectimage.model

import android.net.Uri

sealed interface SelectImageEvent {

    data object NavigateBack : SelectImageEvent

    data class NavigateToSelectLocation(
        val images: List<Uri>,
        val thumbnailImageRatio: Float
    ) : SelectImageEvent
}