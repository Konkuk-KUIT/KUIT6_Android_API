package com.example.kuit6_android_api.data.model.response

import kotlinx.serialization.SerialName

data class PostResponse(
    @SerialName(value = "id") val id: Long,
    @SerialName(value = "title") val title: String,
    @SerialName(value = "content") val content: String,
    @SerialName(value = "imageUrl") val imageUrl: String?, // nullable
    @SerialName(value = "author") val author: AuthorResponse,
    @SerialName(value = "createdAt") val createdAt: String,
    @SerialName(value = "updatedAt") val updatedAt: String
)