package com.andone.memorip.presentation.observer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.model.NetworkStatus
import com.andone.memorip.domain.observer.NetworkObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    networkObserver: NetworkObserver
) : ViewModel() {

    val networkStatus: StateFlow<NetworkStatus> = networkObserver
        .observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = NetworkStatus.Available
        )

    val isNetworkAvailable: Boolean
        get() = networkStatus.value == NetworkStatus.Available
}