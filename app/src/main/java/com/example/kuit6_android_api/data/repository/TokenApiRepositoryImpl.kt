package com.example.kuit6_android_api.data.repository

import com.example.kuit6_android_api.data.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context

@Singleton
class TokenApiRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TokenApiRepository {
    override suspend fun validateToken(context: Context): Result<Boolean> =
    runCatching {
        val response = apiService.validateToken()
        if (response.success && response.data == true) {
            true
        } else {
            throw Exception(response.message ?: "토큰 검증 실패")
        }
    }
}