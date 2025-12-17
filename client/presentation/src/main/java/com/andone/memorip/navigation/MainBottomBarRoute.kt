package com.andone.memorip.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.presentation.R

enum class MainBottomBarRoute(
    @param:DrawableRes val selectedIconId: Int,
    @param:StringRes val titleTextId: Int,
    val route: NavKey
) {
    HOME(
        R.drawable.ic_home,
        R.string.main_bottom_bar_home,
        Home
    ),
    USER(
        R.drawable.ic_account_circle,
        R.string.main_bottom_bar_user,
        User
    )
}