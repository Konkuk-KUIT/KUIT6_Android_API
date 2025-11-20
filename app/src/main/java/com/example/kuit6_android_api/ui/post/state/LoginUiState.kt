package com.example.kuit6_android_api.ui.post.state

//uiState를 이번에는 data class 로 만들어보자.
data class LoginUiState(
    val id: String = "",
    val password: String= "",
    val isAutoLogin: Boolean= false,
    val token: String = "",
)
