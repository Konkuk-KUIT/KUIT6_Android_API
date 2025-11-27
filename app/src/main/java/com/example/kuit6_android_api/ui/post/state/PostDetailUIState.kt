package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostDetailUIState {
    data object Loading : PostDetailUIState()

    data class Success(
        val post: PostResponse
    ) : PostDetailUIState()

    data class Error(
        val message: String
    ) : PostDetailUIState()
}