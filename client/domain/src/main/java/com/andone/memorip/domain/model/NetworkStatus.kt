package com.andone.memorip.domain.model

sealed interface NetworkStatus {
    data object Available : NetworkStatus
    data object Unavailable : NetworkStatus
    data object Losing : NetworkStatus
    data object Lost : NetworkStatus
}