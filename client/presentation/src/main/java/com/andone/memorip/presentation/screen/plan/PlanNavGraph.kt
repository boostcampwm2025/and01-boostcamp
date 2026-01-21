package com.andone.memorip.presentation.screen.plan

import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.andone.memorip.navigation.Plan

fun EntryProviderScope<NavKey>.plan(modifier: Modifier = Modifier) {
    entry<Plan> {
        PlanScreen(modifier = modifier)
    }
}