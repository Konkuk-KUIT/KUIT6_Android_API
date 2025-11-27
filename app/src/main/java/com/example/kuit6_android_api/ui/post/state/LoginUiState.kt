package com.example.kuit6_android_api.ui.post.state

data class LoginUiState(
    val id: String = "",
    val password: String = "",
    val isAutoLogin: Boolean = false,
    val token: String = "",
)
