package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostEditUIState {
    data object Loading : PostEditUIState()

    data class Success(
        val post: PostResponse
    ) : PostEditUIState()

    data class Error(
        val message: String
    ) : PostEditUIState()
}