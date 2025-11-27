package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.App
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.TokenApiRepository
import com.example.kuit6_android_api.data.repository.TokenRepository
import com.example.kuit6_android_api.ui.post.state.LoginUIState
import com.example.kuit6_android_api.ui.post.state.TokenValidationState
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
    private val tokenRepository: TokenRepository,
    private val tokenApiRepository: TokenApiRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

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

    fun initAutoLogin(context: Context) {
        viewModelScope.launch {
            val isAuto = tokenRepository.getAutoLogin()

            _uiState.update { it.copy(isAutoLogin = isAuto) }

            if (isAuto && _uiState.value.tokenValidationState == TokenValidationState.Initial) {
                validateToken(context)
            }
        }
    }

    fun validateToken(context: Context) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    tokenValidationState = TokenValidationState.Initial
                )
            }

            val result = tokenApiRepository.validateToken()

            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess { isValid ->
                if (isValid) {
                    _uiState.update {
                        it.copy(
                            tokenValidationState = TokenValidationState.Success
                        )
                    }
                } else { // 검증 실패
                    tokenRepository.deleteToken() // 토큰 삭제
                    tokenRepository.saveAutoLogin(false) // 자동 로그인 해제
                    _uiState.update {
                        it.copy(
                            tokenValidationState = TokenValidationState.Failure,
                            isAutoLogin = false // UI에도 반영
                        )
                    }
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        tokenValidationState = TokenValidationState.Failure,
                    )
                }
            }
        }
    }
}