package com.andone.memorip.presentation.screen.placeedit.model

import android.content.Context
import android.net.Uri

sealed interface PlaceEditAction {

    data object OnCategoryClick : PlaceEditAction

    data object OnLocationClick : PlaceEditAction

    data object OnTripClick : PlaceEditAction

    data class OnTitleChange(val title: String) : PlaceEditAction

    data class OnContentChange(val content: String) : PlaceEditAction

    data object OnPublicChange : PlaceEditAction

    data class OnImageSelect(val imageUri: Uri) : PlaceEditAction

    data class OnImagesRemove(val imageUri: Uri) : PlaceEditAction

    data class OnScrollPositionChange(val position: Int) : PlaceEditAction

    data class OnPlaceUpdate(val context: Context) : PlaceEditAction

    data class OnSnackBarShow(val message: String) : PlaceEditAction

    data object OnBackClick : PlaceEditAction
}