package com.example.kuit6_android_api.ui.post.state

data class LoginUIState(
    val id: String = "",
    val password: String = "",
    val isAutoLogin: Boolean = false,
    val token: String = "",
    val tokenValidationState: TokenValidationState = TokenValidationState.Initial,
    val isLoading: Boolean = false
)
