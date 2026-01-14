package com.andone.memorip.presentation.screen.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.repository.AuthRepository
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.screen.user.model.UserEvent
import com.andone.memorip.presentation.screen.user.model.LoginMethod
import com.andone.memorip.presentation.screen.user.model.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(val repository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(value = UserUiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<UserEvent>(BUFFERED)
    val event = _event.receiveAsFlow()

    fun onAction(action: UserAction){
        when(action){
            is UserAction.OnMethodClick -> {
                when(action.method){
                    LoginMethod.GOOGLE -> _event.trySend(element = UserEvent.RequestGoogleLogin)
                    LoginMethod.EMAIL -> TODO()
                    LoginMethod.PHONE -> TODO()
                }
            }

            is UserAction.GoogleLoginSuccess -> {
                viewModelScope.launch {
                    repository.signInWithGoogle(idToken = action.idToken)
                        .onSuccess {
                            _uiState.update {
                                it.copy(isLoggedIn = true)
                            }
                        }
                        .onFailure { e ->
                            _uiState.update {
                                it.copy(errorMessage = e.message)
                            }
                        }
                }
            }
        }
    }
}