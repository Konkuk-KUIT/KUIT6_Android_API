package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostDeleteUIState {
    data object Loading : PostDeleteUIState()

    data class Success(
        val post: Unit
    ) : PostDeleteUIState()

    data class Error(
        val message: String
    ) : PostDeleteUIState()
}