package com.example.kuit6_android_api.ui.post.state

sealed class ImageUiState {
    data object Idle : ImageUiState() // 업로드가 아닌 일반 상태
    data object Loading : ImageUiState() // 업로딩 상태

    data class Success(
        val imgUrl: Map<String, String>
    ) : ImageUiState()

    data class Error(
        val message: String
    ) : ImageUiState()
}
