package com.example.kuit6_android_api.data.repository

import javax.inject.Singleton

import com.example.kuit6_android_api.data.model.response.BaseResponse

interface TokenApiRepository{
    suspend fun getValidateTokenApi(): Result<BaseResponse<Boolean>>
    suspend fun validateToken():Result<Boolean>
}