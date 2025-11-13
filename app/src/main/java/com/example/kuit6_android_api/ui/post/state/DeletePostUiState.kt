
package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class DeletePostUiState {
    data object Loading : DeletePostUiState()

    data class Success(
        val deletePost: PostResponse
    ) : DeletePostUiState()

    data class Error(
        val message: String
    ) : DeletePostUiState()
}
