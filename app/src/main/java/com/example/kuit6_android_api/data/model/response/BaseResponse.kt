package com.example.kuit6_android_api.data.model.response

import kotlinx.serialization.SerialName

data class BaseResponse<T>(
    @SerialName(value = "success") val success: Boolean,
    @SerialName(value = "message") val message: String?,
    @SerialName(value = "data") val data: T?, //nullable
    @SerialName(value = "timestamp") val timestamp: String
)