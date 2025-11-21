package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostCreateUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostCreateViewModel (
    private val postRepository : PostRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow<PostCreateUiState>(PostCreateUiState.Loading) // 변경 가능 상태
    val uiState: StateFlow<PostCreateUiState> = _uiState.asStateFlow()
    fun createPost(
        author: String,
        request: PostCreateRequest
    ){
        viewModelScope.launch {
            _uiState.value = PostCreateUiState.Loading

            postRepository.createPost(author, request)
                .onSuccess { post ->
                    _uiState.value = PostCreateUiState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostCreateUiState.Error(
                        error.message ?: "error"
                    )
                }
        }
    }
}


