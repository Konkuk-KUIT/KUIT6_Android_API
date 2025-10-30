package com.example.kuit6_android_api.data.model.response

import android.R.attr.value
import kotlinx.serialization.SerialName

data class BaseResponse<T>(
    @SerialName (value= "success") val success: Boolean,
    @SerialName (value= "message") val message: String?,
    @SerialName (value= "data") val data: T?,
    @SerialName (value= "timestamp") val timestamp: String
)