package com.andone.memorip.presentation.common

import androidx.compose.material3.SnackbarDuration
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

@Singleton
class SnackbarManager @Inject constructor() {
    private val _message = Channel<SnackBarRequest>(capacity = BUFFERED)
    val message = _message.receiveAsFlow()

    fun show(content: SnackBarRequest) {
        _message.trySend(content)
    }
}