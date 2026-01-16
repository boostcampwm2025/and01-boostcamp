package com.andone.memorip.presentation.screen.plan

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.Plan

fun EntryProviderScope<NavKey>.plan() {
    entry<Plan> {
        PlanScreen()
    }
}