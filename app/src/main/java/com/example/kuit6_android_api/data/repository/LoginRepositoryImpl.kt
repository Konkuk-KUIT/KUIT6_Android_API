package com.example.kuit6_android_api.data.repository

import android.util.Log
import com.example.kuit6_android_api.data.model.request.LoginRequest
import com.example.kuit6_android_api.data.model.response.LoginResponse
import com.example.kuit6_android_api.data.service.ApiService

class LoginRepositoryImpl(
    private val apiService: ApiService
) : LoginRepository {
    override suspend fun signup(id: String, password: String): Result<LoginResponse> {
        return runCatching {
            val response = apiService.signup(
                LoginRequest(
                    username = id, password = password
                )
            )

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "회원 가입 실패")
            }
        }.onFailure { error ->
            Log.e("LoginRepository", error.message.toString())
        }
    }

    override suspend fun login(id: String, password: String): Result<LoginResponse> {
        return runCatching {
            val response = apiService.login(
                LoginRequest(
                    username = id, password = password
                )
            )

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "로그인 실패")
            }
        }.onFailure { error ->
            Log.e("LoginRepository", error.message.toString())
        }
    }

    override suspend fun validate(): Result<Boolean> {
        return runCatching {
            val response = apiService.validateToken()

            if (response.success && response.data != null)
                response.data
            else
                throw Exception(response.message ?: "토큰 검증 실패")
        }
    }
}