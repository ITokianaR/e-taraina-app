package com.example.e_taraina.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_taraina.di.AppModule
import com.example.e_taraina.domain.models.UserRole
import com.example.e_taraina.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface LoginEvent {
    data class NavigateHome(val username: String, val role: UserRole) : LoginEvent
}

/**
 * @JvmOverloads generates a no-arg constructor from the default
 * parameter, so Compose's `viewModel()` can instantiate this via
 * reflection without a custom factory.
 */
class LoginViewModel @JvmOverloads constructor(
    private val loginUseCase: LoginUseCase = AppModule.loginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onLoginClick() {
        val current = _uiState.value
        if (current.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            loginUseCase(current.username, current.password)
                .onSuccess { user ->
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(LoginEvent.NavigateHome(user.username, user.role))
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message ?: "Login failed")
                    }
                }
        }
    }
}
