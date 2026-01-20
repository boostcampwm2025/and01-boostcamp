package com.andone.memorip.presentation.screen.plan

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.screen.plan.model.PlanAction
import com.andone.memorip.presentation.screen.plan.model.PlanUiState
import com.andone.memorip.presentation.util.DummyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor() : ViewModel() {

    val _uiState = MutableStateFlow(value = PlanUiState(blocks = DummyData.timeBlocks))
    val uiState = _uiState.asStateFlow()

    fun onAction(action: PlanAction) {
        when (action) {
            is PlanAction.BlockMoved -> {moveBlock(action.id, action.newStartMinute)}
        }
    }
    private fun moveBlock(id: String, newStartMinute: Int) {
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
