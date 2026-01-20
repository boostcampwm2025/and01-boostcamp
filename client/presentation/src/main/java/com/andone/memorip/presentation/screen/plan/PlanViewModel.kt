package com.andone.memorip.presentation.screen.plan

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.util.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class PlanViewModel : ViewModel() {

    val _uiState = MutableStateFlow(value = PlanUiState(blocks = DummyData.timeBlocks))
    val uiState = _uiState.asStateFlow()

    fun moveBlock(id: String, newStartMinute: Int) {
        _uiState.update {
            it.copy(
                blocks = it.blocks.map { block ->
                    if (block.id == id) block.copy(startMinute = newStartMinute)
                    else block
                }
            )
        }
    }
}
