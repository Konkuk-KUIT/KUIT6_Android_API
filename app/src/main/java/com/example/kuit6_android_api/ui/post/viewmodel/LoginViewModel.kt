package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.App
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.TokenRepository
import com.example.kuit6_android_api.ui.post.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository,//새 파라미터
    private val application: App//init에서 자동 로그인 확인에 사용
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onIdChanged(id: String) {
        _uiState.update {
            it.copy(id = id)
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun onAutoLoginChanged(isAutoLogin: Boolean, context: Context) {
        _uiState.update {
            it.copy(isAutoLogin = isAutoLogin)
        }
        viewModelScope.launch {
            //뷰모델 생명주기에 맞춰 코루틴 실행
            tokenRepository.saveAutoLogin(context, isAutoLogin)
            //Context 파라미터를 통해 자동 로그인 값 변경 감지
            //자동 로그인 값 변경 감지되면 DataStore에 저장하는 saveAutoLogin의 로직 수행
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
            }
        }
    }

    fun getToken(context: Context) {
        viewModelScope.launch {
            val token = tokenRepository.getToken(context)
            _uiState.update {
                it.copy(token = token ?: "")
            }
        }
    }
    
    fun verifyToken(context: Context) {
        //토큰 검증 수행 함수
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isVerifying = true,
                    verificationButtonText = "토큰 검증 중..."
                )
            }
            
            loginRepository.verifyToken()
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isVerifying = false,
                            verificationButtonText = "토큰 검증 성공"
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isVerifying = false,
                            verificationButtonText = "토큰 검증 실패"
                        )
                    }
                }
        }
    }

    init{
        //자동 로그인되어 있으면 UI 상태 업데이트하고 토큰 검증
        viewModelScope.launch {
            val isAutoLogin = tokenRepository.getAutoLogin(application)
            if (isAutoLogin) {
                _uiState.update {
                    it.copy(isAutoLogin = true)
                }
                verifyToken(application)
            }
        }
    }
}