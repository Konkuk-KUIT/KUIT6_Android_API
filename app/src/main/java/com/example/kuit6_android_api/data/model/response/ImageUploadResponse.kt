package com.example.kuit6_android_api.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadResponse(
    @SerialName("imageUrl") val imageUrl: String
)
