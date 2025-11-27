package com.example.kuit6_android_api.data.repository

interface TokenRepository {
    suspend fun saveToken(token: String)

    suspend fun getToken(): String?

    suspend fun saveAutoLogin(isAutoLogin: Boolean)
    suspend fun getAutoLogin(): Boolean
}