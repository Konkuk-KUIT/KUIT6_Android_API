package com.example.kuit6_android_api.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorResponse(
    @SerialName("id") val id: Long,
    @SerialName("username") val username: String,
    @SerialName("profileImageUrl") val profileImageUrl: String?
)