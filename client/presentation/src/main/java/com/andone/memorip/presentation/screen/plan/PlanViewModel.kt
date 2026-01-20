package com.andone.memorip.presentation.screen.plan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.screen.plan.model.TimeBlock

class PlanViewModel : ViewModel() {

    var uiState by mutableStateOf(
        PlanUiState(
            blocks = listOf(
                TimeBlock("1", 9 * 60, 60)
            )
        )
    )
        private set

    fun moveBlock(id: String, newStartMinute: Int) {
        uiState = uiState.copy(
            blocks = uiState.blocks.map {
                if (it.id == id) it.copy(startMinute = newStartMinute)
                else it
            }
        )
    }
}
