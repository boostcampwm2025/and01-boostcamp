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
    PLACE_LIST(
        R.drawable.ic_home,
        R.string.main_bottom_bar_place_list,
        PlaceList
    ),
    PLAN(
        R.drawable.ic_calendar_today,
        R.string.main_bottom_bar_plan,
        Plan
    ),
    GROUP_LIST(
        R.drawable.ic_folder,
        R.string.main_bottom_bar_group,
        GroupList
    ),
    USER(
        R.drawable.ic_account_circle,
        R.string.main_bottom_bar_user,
        User
    )
}