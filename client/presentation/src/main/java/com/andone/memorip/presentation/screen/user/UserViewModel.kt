package com.andone.memorip.presentation.screen.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andone.memorip.domain.auth.TokenRefresher
import com.andone.memorip.domain.repository.AuthRepository
import com.andone.memorip.domain.repository.UserRepository
import com.andone.memorip.presentation.screen.user.model.UserAction
import com.andone.memorip.presentation.screen.user.model.UserEvent
import com.andone.memorip.presentation.screen.user.model.LoginMethod
import com.andone.memorip.presentation.screen.user.model.UserUiState
import com.andone.memorip.presentation.screen.user.model.toUiModel
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
class UserViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRefresher: TokenRefresher,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(value = UserUiState(isLoggedIn = authRepository.isLoggedIn()))
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<UserEvent>(BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        if (authRepository.isLoggedIn()) {
            updateUser()
        }
    }

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.OnMethodClick -> {
                when (action.method) {
                    LoginMethod.GOOGLE -> _event.trySend(element = UserEvent.RequestGoogleLogin)
                    LoginMethod.EMAIL -> {
                        _uiState.update {
                            it.copy(showLoginDialog = true)
                        }
                    }

                    LoginMethod.PHONE -> {}
                }
            }

            is UserAction.GoogleLoginSuccess -> {
                viewModelScope.launch {
                    authRepository.signInWithGoogle(idToken = action.idToken)
                        .onSuccess {
                            _uiState.update {
                                it.copy(isLoggedIn = true)
                            }
                            tokenRefresher.refreshToken(force = true)
                            updateUser()
                        }
                        .onFailure { e ->
                            _uiState.update {
                                it.copy(errorMessage = e.message)
                            }
                        }
                }
            }

            UserAction.CloseLoginDialog -> {
                _uiState.update {
                    it.copy(showLoginDialog = false)
                }
            }

            is UserAction.UpdateEmail -> {
                _uiState.update {
                    it.copy(email = action.email)
                }
            }

            is UserAction.UpdatePassword -> {
               _uiState.update {
                   it.copy(password = action.password)
               }
            }

            is UserAction.UpdatePasswordConfirm -> {
                _uiState.update {
                    it.copy(passwordConfirm = action.passwordConfirm)
                }
            }

            UserAction.ToggleLoginMode -> {
                _uiState.update {
                    it.copy(isNewAccount = !it.isNewAccount)
                }
            }

            is UserAction.EmailLoginSubmit -> {

            }
        }
    }

    private fun updateEmail() {
        viewModelScope.launch {
            authRepository.getEmail()
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(user = it.user.copy(email = data))
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(errorMessage = e.message)
                    }
                }
        }
    }

    private fun updateUser() {
        viewModelScope.launch {
            userRepository.getMe()
                .onSuccess { data ->
                    _uiState.update {
                        it.copy(user = data.toUiModel())
                    }
                    updateEmail()
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(errorMessage = e.message)
                    }
                }
        }
    }
}