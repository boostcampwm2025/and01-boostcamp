package com.andone.memorip.presentation.screen.placecreate.model

import android.content.Context
import android.net.Uri
import com.andone.memorip.presentation.model.GroupUiModel

sealed interface PlaceCreateAction {

    data object OnCategoryClick : PlaceCreateAction

    data object OnLocationClick : PlaceCreateAction

    data object OnGroupClick : PlaceCreateAction

    data class OnTitleChange(val title: String) : PlaceCreateAction

    data class OnContentChange(val content: String) : PlaceCreateAction

    data object OnPublicChange : PlaceCreateAction

    data class OnImageSelect(val imageUri: Uri) : PlaceCreateAction

    data class OnImagesRemove(val imageUri: Uri) : PlaceCreateAction

    data object OnLastImageRemove : PlaceCreateAction

    data class OnScrollPositionChange(val position: Int) : PlaceCreateAction

    data class OnPlaceCreate(val context: Context) : PlaceCreateAction

    data class OnSnackBarShow(val message: String) : PlaceCreateAction

    data class OnGroupSelect(val groups: List<GroupUiModel>) : PlaceCreateAction

    data object OnCreateSuccess : PlaceCreateAction
}