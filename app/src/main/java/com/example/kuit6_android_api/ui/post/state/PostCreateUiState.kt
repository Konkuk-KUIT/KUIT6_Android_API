package com.example.kuit6_android_api.ui.post.state

sealed class PostCreateUiState {
    data object Loading: PostCreateUiState()

    data class Success(
        val uploadedImageUrl: String? = null,
        val isUploading: Boolean = false
    ): PostCreateUiState()

    data class Error(
        val message: String
    ): PostCreateUiState()
}

