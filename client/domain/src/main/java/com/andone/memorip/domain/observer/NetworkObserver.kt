package com.andone.memorip.domain.observer

import com.andone.memorip.domain.model.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    fun observe(): Flow<NetworkStatus>
}