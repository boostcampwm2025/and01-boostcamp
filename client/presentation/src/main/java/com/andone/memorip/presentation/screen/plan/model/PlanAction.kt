package com.andone.memorip.presentation.screen.plan.model

sealed interface PlanAction {
    data class BlockMoved(val id: String, val newStartMinute: Int) : PlanAction
}
