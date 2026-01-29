package com.andone.memorip.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.presentation.R

enum class MainBottomBarRoute(
    @param:DrawableRes val selectedIconId: Int,
    @param:DrawableRes val unselectedIconId: Int,
    @param:StringRes val titleTextId: Int,
    val route: NavKey
) {
    PLACE_LIST(
        selectedIconId = R.drawable.ic_twotone_home,
        unselectedIconId = R.drawable.ic_outline_home,
        titleTextId = R.string.main_bottom_bar_place_list,
        route = PlaceList
    ),
    GROUP_LIST(
        selectedIconId = R.drawable.ic_twotone_folder,
        unselectedIconId = R.drawable.ic_outline_folder,
        titleTextId = R.string.main_bottom_bar_group,
        route = GroupList
    ),
    PLAN(
        selectedIconId = R.drawable.ic_twotone_calendar_month,
        unselectedIconId = R.drawable.ic_outline_calendar_month,
        titleTextId = R.string.main_bottom_bar_plan,
        route = Plan
    ),
    USER(
        selectedIconId = R.drawable.ic_twotone_account_circle,
        unselectedIconId = R.drawable.ic_outline_account_circle,
        titleTextId = R.string.main_bottom_bar_user,
        route = User
    )
}