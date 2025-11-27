package com.example.kuit6_android_api.data.repository

import android.content.Context
import com.example.kuit6_android_api.data.model.response.BaseResponse
interface TokenRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun saveAutoLogin(enabled: Boolean)
    suspend fun getAutoLogin(): Boolean
    suspend fun deleteToken()
}