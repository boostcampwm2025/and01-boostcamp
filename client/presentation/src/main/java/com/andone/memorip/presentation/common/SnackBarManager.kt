package com.andone.memorip.presentation.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Stable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

data class SnackBarRequest(
    val message: String,
    val actionLabel: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onAction: (suspend () -> Unit)? = null
)

@Stable
@Singleton
class SnackBarManager @Inject constructor() {
    private val _message = Channel<SnackBarRequest>(capacity = BUFFERED)
    val message = _message.receiveAsFlow()

    private var lastRequest: SnackBarRequest? = null
    private var lastRequestTime = 0L

    fun show(request: SnackBarRequest) {
        if (isDuplicateRequest(request = request)) return

        updateLastRequest(request = request)
        _message.trySend(element = request)
    }

    private fun isDuplicateRequest(request: SnackBarRequest): Boolean {
        val last = lastRequest ?: return false
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - lastRequestTime

        return last.message == request.message &&
                last.actionLabel == request.actionLabel &&
                timeDifference < DUPLICATE_PREVENTION_INTERVAL_MS
    }

    private fun updateLastRequest(request: SnackBarRequest) {
        lastRequest = request
        lastRequestTime = System.currentTimeMillis()
    }

    companion object {
        private const val DUPLICATE_PREVENTION_INTERVAL_MS = 4_000L
    }
}