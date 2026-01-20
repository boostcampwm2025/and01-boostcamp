package com.andone.memorip.presentation.screen.plan.model

sealed interface PlanAction {
    data class BlockMoved(val id: String, val newStartMinute: Int) : PlanAction

    object AddDay : PlanAction

    data class RemoveDay(val day: Int) : PlanAction

    data class LongClick(val day: Int) : PlanAction

    data class SelectDay(val day: Int) : PlanAction
}
