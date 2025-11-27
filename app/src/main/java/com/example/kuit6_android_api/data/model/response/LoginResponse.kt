package com.example.kuit6_android_api.data.model.response

data class LoginResponse(
    val token: String,
    val userId: Long,
    val username: String,
)
