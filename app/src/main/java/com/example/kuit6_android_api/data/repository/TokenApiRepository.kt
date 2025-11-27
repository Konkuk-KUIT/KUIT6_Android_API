package com.example.kuit6_android_api.data.repository

interface TokenApiRepository {
    suspend fun validateToken() : Result<Boolean>
}