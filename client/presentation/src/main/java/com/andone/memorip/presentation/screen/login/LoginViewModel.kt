package com.andone.memorip.presentation.screen.login

import androidx.lifecycle.ViewModel
import com.andone.memorip.domain.repository.AuthRepository
import com.andone.memorip.presentation.screen.login.model.LoginAction
import com.andone.memorip.presentation.screen.login.model.LoginEvent
import com.andone.memorip.presentation.screen.login.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(value = LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<LoginEvent>(BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: LoginAction){
        when(action){
            is LoginAction.OnMethodClick -> TODO()
        }
    }
}