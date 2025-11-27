package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.TokenRepository
import com.example.kuit6_android_api.ui.post.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onIdChanged(id: String) {
        _uiState.update { it.copy(id = id) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onAutoLoginChanged(context: Context, isAutoLogin: Boolean) {
        _uiState.update { it.copy(isAutoLogin = isAutoLogin) }
        viewModelScope.launch {
            tokenRepository.saveAutoLogin(isAutoLogin)
        }
    }

    fun signup(context: Context) {
        viewModelScope.launch {
            loginRepository.signup(
                id = uiState.value.id,
                password = uiState.value.password
            ).onSuccess {
                tokenRepository.saveToken(it.token)
            }
        }
    }

    fun login(context: Context) {
        viewModelScope.launch {
            loginRepository.login(
                id = uiState.value.id,
                password = uiState.value.password
            ).onSuccess {
                tokenRepository.saveToken(it.token)
            }
        }
    }

    fun getToken(context: Context) {
        viewModelScope.launch {
            val token = tokenRepository.getToken()
            _uiState.update { it.copy(token = token ?: "") }
        }
    }

    fun validateToken(onResult: (Boolean) -> Unit){
        viewModelScope.launch {
            loginRepository.validate()
                .onSuccess { isValid ->
                    onResult(isValid)
                }
                .onFailure {
                    onResult(false)
                }
        }
    }

    fun checkAutoLoginAndValidate(
        context: Context,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val isAuto = tokenRepository.getAutoLogin()
            _uiState.update { it.copy(isAutoLogin = isAuto) }

            if (isAuto){
                loginRepository.validate()
                    .onSuccess { isValid ->
                        onResult(isValid)
                    }
                    .onFailure {
                        onResult(false)
                    }
            }
        }
    }
}