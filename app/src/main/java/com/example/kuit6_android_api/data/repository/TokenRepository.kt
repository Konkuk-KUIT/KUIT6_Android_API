package com.example.kuit6_android_api.data.repository

import android.content.Context

interface TokenRepository {
    suspend fun saveToken(context: Context, token: String)

    suspend fun getToken(context: Context): String?

    suspend fun saveAutoLogin(context: Context, isAutoLogin: Boolean)
    suspend fun getAutoLogin(context: Context): Boolean
}