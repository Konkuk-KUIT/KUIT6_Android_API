package com.example.kuit6_android_api.ui.post.state

data class LoginUiState(
    val id: String = "",
    val password: String = "",
    val isAutoLogin: Boolean = false,
    val token: String = "",
    val verificationButtonText: String = "토큰 검증",//검증 버튼 텍스트
    val isVerifying: Boolean = false//검증 진행 중 여부
)
