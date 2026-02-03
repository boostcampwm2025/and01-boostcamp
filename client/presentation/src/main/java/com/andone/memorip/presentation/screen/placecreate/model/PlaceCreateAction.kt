package com.andone.memorip.presentation.screen.placecreate.model

import android.content.Context
import android.net.Uri
import com.andone.memorip.presentation.model.TripUiModel

sealed interface PlaceCreateAction {

    data object OnCategoryClick : PlaceCreateAction

    data object OnLocationClick : PlaceCreateAction

    data object OnTripClick : PlaceCreateAction

    data class OnTitleChange(val title: String) : PlaceCreateAction

    data class OnContentChange(val content: String) : PlaceCreateAction

    data object OnPublicChange : PlaceCreateAction

    data class OnImageSelect(val imageUri: Uri) : PlaceCreateAction

    data class OnImagesRemove(val imageUri: Uri) : PlaceCreateAction

    data class OnScrollPositionChange(val position: Int) : PlaceCreateAction

    data class OnPlaceCreate(val context: Context) : PlaceCreateAction

    data class OnSnackBarShow(val message: String) : PlaceCreateAction

    data class OnTripSelect(val trips: List<TripUiModel>) : PlaceCreateAction

    data object OnCreateSuccess : PlaceCreateAction
}