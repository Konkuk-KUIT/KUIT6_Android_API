package com.example.kuit6_android_api.data.model.response

import kotlinx.serialization.SerialName

data class AuthorResponse(
    @SerialName(value = "id") val id: Long,
    @SerialName(value = "username") val username: String,
    @SerialName(value = "profileImageUrl") val profileImageUrl: String? // nullable
)