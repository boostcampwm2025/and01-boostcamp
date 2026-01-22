package com.andone.memorip.presentation.screen.selectimage.model

import android.net.Uri

sealed interface SelectImageAction {

    data class OnImagesSelect(val images: List<Uri>) : SelectImageAction

    data class OnImagesCrop(val images: List<Uri>) : SelectImageAction

    data object OnBack : SelectImageAction
}