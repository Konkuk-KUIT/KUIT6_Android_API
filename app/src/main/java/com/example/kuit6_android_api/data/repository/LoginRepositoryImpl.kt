package com.example.kuit6_android_api.data.repository

import android.util.Log
import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.model.request.LoginRequest
import com.example.kuit6_android_api.data.model.response.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl
    @Inject
    constructor(
        private val apiService: ApiService, // RetrofitClient.apiService로 접근하던 기존과 달리 자동 주입
    ) : LoginRepository {
        override suspend fun signup(
            id: String,
            password: String,
        ): Result<LoginResponse> =
            runCatching {
                val response =
                    apiService.signup(
                        LoginRequest(
                            username = id,
                            password = password,
                        ),
                    )

                if (response.success && response.data != null) {
                    response.data
                } else {
                    throw Exception(response.message ?: "회원가입 실패")
                }
            }.onFailure { error ->
                Log.e("LoginRepository", error.message.toString())
            }

        override suspend fun login(
            id: String,
            password: String,
        ): Result<LoginResponse> =
            runCatching {
                val response =
                    apiService.login(
                        LoginRequest(
                            username = id,
                            password = password,
                        ),
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
