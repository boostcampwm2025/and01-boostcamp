package com.andone.memorip.presentation.home

import androidx.lifecycle.ViewModel
import com.andone.memorip.presentation.home.model.GroupUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(value = listOf(GroupUiModel.default()))
    val uiState: StateFlow<List<GroupUiModel>> = _uiState

}