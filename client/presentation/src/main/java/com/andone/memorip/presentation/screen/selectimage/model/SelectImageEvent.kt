package com.andone.memorip.presentation.screen.selectimage.model

import android.net.Uri

sealed interface SelectImageEvent {

    data class NavigateToSelectLocation(val images: List<Uri>) : SelectImageEvent
}