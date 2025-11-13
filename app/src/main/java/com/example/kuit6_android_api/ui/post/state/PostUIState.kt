package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostUIState {
    data object Loading: PostUIState()

    data class Success(
        val post: PostResponse
    ): PostUIState()

    data class Error(
        val message: String
    ): PostUIState()
}