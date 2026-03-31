package com.example.kuit6_android_api.data.repository

import android.content.Context
import javax.inject.Singleton

interface TokenApiRepository {
    suspend fun validateToken(context: Context): Result<Boolean>
}