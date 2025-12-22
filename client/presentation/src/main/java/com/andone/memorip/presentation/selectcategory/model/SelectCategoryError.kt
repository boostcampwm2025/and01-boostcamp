package com.andone.memorip.presentation.selectcategory.model

import android.content.Context
import com.andone.memorip.presentation.R

sealed class SelectCategoryError {
    data object MaxCategoryOverError : SelectCategoryError()
}

fun SelectCategoryError.toErrorMessage(context: Context): String {
    return when (this) {
        SelectCategoryError.MaxCategoryOverError -> context.getString(R.string.select_category_max_category_over_err_message)
    }
}