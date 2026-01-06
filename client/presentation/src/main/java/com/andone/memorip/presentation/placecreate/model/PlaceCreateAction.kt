package com.andone.memorip.presentation.placecreate.model

import android.net.Uri

sealed interface PlaceCreateAction {

    data object OnCategoryClick : PlaceCreateAction

    data object OnLocationClick : PlaceCreateAction

    data object OnGroupClick : PlaceCreateAction

    data class OnTitleChange(val title: String) : PlaceCreateAction

    data class OnContentChange(val content: String) : PlaceCreateAction

    data class OnImagesAdd(val images: List<Uri>) : PlaceCreateAction

    data class OnImagesRemove(val imageUri: Uri) : PlaceCreateAction

    data object OnPlaceCreate : PlaceCreateAction

    data object OnSnackBarShow : PlaceCreateAction

    data object OnBackClick : PlaceCreateAction
}