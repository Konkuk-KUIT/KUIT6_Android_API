package com.example.kuit6_android_api.data.model.request

import kotlinx.serialization.SerialName

data class PostCreateRequest(
    @SerialName(value = "title") val title: String,
    @SerialName(value = "content") val content: String,
    @SerialName(value = "imageUrl") val imageUrl: String? // nullable
)