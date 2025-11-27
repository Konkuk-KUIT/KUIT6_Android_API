package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostCreateUIState {
    data object Loading: PostCreateUIState()

    data class Success(
        val post: PostResponse
    ): PostCreateUIState()

    data class Error(
        val message: String
    ): PostCreateUIState()
}