package com.example.kuit6_android_api.ui.post.state

import com.example.kuit6_android_api.data.model.response.PostResponse

sealed class PostListUIState{
    data object Loading : PostListUIState()

    data class Success(
        val posts : List<PostResponse>
    ): PostListUIState()

    data class Error(
        val message:String
    ): PostListUIState()
}