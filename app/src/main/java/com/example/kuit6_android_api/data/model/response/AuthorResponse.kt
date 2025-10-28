package com.example.kuit6_android_api.data.model.response

import kotlinx.serialization.SerialName

data class AuthorResponse(
    @SerialName(value = "id") val userID: Long,
    val username: String,
    val profileImageUrl: String?
)

