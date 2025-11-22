package com.example.kuit6_android_api.data.repository

import com.example.kuit6_android_api.data.model.response.LoginResponse
import com.example.kuit6_android_api.data.model.response.PostResponse

interface LoginRepository {
    suspend fun signup(id:String,password:String):Result<LoginResponse>
    suspend fun login(id:String,password:String):Result<LoginResponse>
    suspend fun verifyToken():Result<Unit>//토큰 검증 수행 함수
}