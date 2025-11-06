package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class CreateUiState {
    //작업 안 한 상태
    data object Idle : CreateUiState()
    //게시글 작성 중
    data object Loading : CreateUiState()
    data class Success(val post: PostResponse) : CreateUiState()
    data class Error(val message: String) : CreateUiState()

}