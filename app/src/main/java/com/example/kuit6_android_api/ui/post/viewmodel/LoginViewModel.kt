package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.App
import com.example.kuit6_android_api.data.repository.LoginRepository
import com.example.kuit6_android_api.data.repository.TokenApiRepository
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
class LoginViewModel @Inject constructor(// 파라미터를 Hilt가 자동 주입
    private val loginRepository: LoginRepository,
    private val tokenRepository: TokenRepository,
    private val tokenApiRepository: TokenApiRepository
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
            tokenRepository.saveAutoLogin(isAutoLogin)
            //자동 로그인 값 변경 감지되면 DataStore에 저장하는 saveAutoLogin의 로직 수행
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
            _uiState.update {
                it.copy(token = token ?: "")
            }
        }
    }
    
    fun validateToken(context: Context) {
        viewModelScope.launch {
            // 검증 시작 상태 업데이트
            _uiState.update {
                it.copy(
                    isVerifying = true,
                    verificationButtonText = "토큰 검증 중..."
                )
            }
            
            // 토큰 검증 API 호출
            val result = tokenApiRepository.validateToken(context)
            
            // 로딩 완료
            _uiState.update {
                it.copy(isVerifying = false)
            }
            
            result.onSuccess { isValid ->
                // 검증 성공
                if (isValid) {
                    _uiState.update {
                        it.copy(
                            verificationButtonText = "토큰 검증 성공"
                        )
                    }
                } else {
                    // 검증 실패
                    tokenRepository.deleteToken() // 토큰 삭제
                    tokenRepository.saveAutoLogin(false) // 자동 로그인 해제
                    _uiState.update {
                        it.copy(
                            verificationButtonText = "토큰 검증 실패",
                            isAutoLogin = false // UI에도 반영
                        )
                    }
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        verificationButtonText = "토큰 검증 실패"
                    )
                }
            }
        }
    }

    init{
        //자동 로그인되어 있으면 UI 상태 업데이트하고 토큰 검증
        viewModelScope.launch {
            val isAutoLogin = tokenRepository.getAutoLogin()
            if (isAutoLogin) {
                _uiState.update {
                    it.copy(isAutoLogin = true)
                }
                // init 블록에서는 context를 사용할 수 없으므로 validateToken 호출 제거
                // 필요시 Screen에서 호출하도록 변경
            }
        }
    }
}