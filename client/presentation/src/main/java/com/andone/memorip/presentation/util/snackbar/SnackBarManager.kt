package com.andone.memorip.presentation.util.snackbar

import androidx.compose.runtime.Stable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Stable
@Singleton
class SnackBarManager @Inject constructor() {
    private val _message = Channel<SnackBarEvent>(capacity = BUFFERED)
    val message = _message.receiveAsFlow()

    private var lastEvent: SnackBarEvent? = null
    private var lastEventTime = 0L

    fun show(event: SnackBarEvent) {
        if (isDuplicateEvent(event = event)) return

        updateLastEvent(event = event)
        _message.trySend(element = event)
    }

    private fun isDuplicateEvent(event: SnackBarEvent): Boolean {
        val last = lastEvent ?: return false
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - lastEventTime

        return last.messageResId == event.messageResId &&
            last.action?.labelResId == event.action?.labelResId &&
            timeDifference < DUPLICATE_PREVENTION_INTERVAL_MS
    }

    private fun updateLastEvent(event: SnackBarEvent) {
        lastEvent = event
        lastEventTime = System.currentTimeMillis()
    }

    companion object {
        private const val DUPLICATE_PREVENTION_INTERVAL_MS = 4_000L
    }
}