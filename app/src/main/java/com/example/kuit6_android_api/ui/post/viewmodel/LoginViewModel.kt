package com.example.kuit6_android_api.ui.post.viewmodel

import android.R.attr.password
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.TokenRepository
import com.example.kuit6_android_api.ui.post.state.LoginUiState
import com.example.kuit6_android_api.ui.post.state.TokenValidationResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // init에서는 Context가 필요한 작업을 하지 않음
    // UI에서 LaunchedEffect로 loadAutoLoginSetting 호출

    fun onIdChanged(id: String) {
        _uiState.update {
            it.copy(id = id)
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onAutoLoginChanged(isAutoLogin: Boolean, context: Context) {
        _uiState.update { it.copy(isAutoLogin = isAutoLogin) }
        // DataStore에 자동 로그인 설정 저장
        viewModelScope.launch {
            tokenRepository.saveAutoLogin(context, isAutoLogin)
        }
    }

    fun signup(context: Context) {
        viewModelScope.launch {
            loginRepository.signup(
                id = uiState.value.id,
                password = uiState.value.password
            ).onSuccess {
                tokenRepository.saveToken(context, it.token)
            }
        }
    }

    fun login(context: Context) {
        viewModelScope.launch {
            loginRepository.login(
                id = uiState.value.id,
                password = uiState.value.password
            ).onSuccess {
                tokenRepository.saveToken(context, it.token)
                // 로그인 성공 시 자동 로그인이 체크되어 있으면 자동 토큰 검증 실행
                if (uiState.value.isAutoLogin) {
                    validateToken(context)
                }
            }
        }
    }

    fun getToken(context: Context){
        viewModelScope.launch{
            val token = tokenRepository.getToken(context)
            _uiState.update{it.copy(token = token ?: "")}
        }
    }

    fun validateToken(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isTokenValidating = true, tokenValidationResult = null) }
            
            loginRepository.validateToken()
                .onSuccess { isValid ->
                    _uiState.update { 
                        it.copy(
                            isTokenValidating = false,
                            tokenValidationResult = if (isValid) TokenValidationResult.Success 
                                                  else TokenValidationResult.Error("토큰이 유효하지 않습니다")
                        )
                    }
                     // 3초 후 버튼을 원래 상태로 되돌리기
                delay(3000)
                _uiState.update { it.copy(tokenValidationResult = null) }
                }
                .onFailure { error ->
                    _uiState.update { 
                        it.copy(
                            isTokenValidating = false,
                            tokenValidationResult = TokenValidationResult.Error(error.message ?: "토큰 검증 실패")
                        )
                    }
                     // 3초 후 버튼을 원래 상태로 되돌리기
                delay(3000)
                _uiState.update { it.copy(tokenValidationResult = null) }
                }
        }
    }


    fun loadAutoLoginSetting(context: Context) {
        viewModelScope.launch {
            val isAutoLogin = tokenRepository.getAutoLogin(context)
            _uiState.update { it.copy(isAutoLogin = isAutoLogin) }
            
            // 자동 로그인이 활성화되어 있으면 자동으로 토큰 검증 실행
            if (isAutoLogin) {
                validateToken(context)
            }
        }
    }
}