package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostCreateUiState {
    data object Idle : PostCreateUiState() // 기본
    data object Loading : PostCreateUiState()
    data object IsUploading: PostCreateUiState() // 사진 업로드 중

    data class Success(
        val post: PostResponse
    ) : PostCreateUiState()

    data class Error(
        val message: String
    ) : PostCreateUiState()
}