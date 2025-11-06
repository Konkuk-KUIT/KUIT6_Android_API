package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.repository.PostRepository
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.ui.post.state.CreateUiState
import com.example.kuit6_android_api.ui.post.state.PostDetailUiState
import com.example.kuit6_android_api.ui.post.state.PostEditUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostEditViewModel(
    val postRepository: PostRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PostEditUiState>(PostEditUiState.Loading)
    val uiState: StateFlow<PostEditUiState> = _uiState.asStateFlow()

    fun loadEditPost(
        id: Long,
        request: PostCreateRequest
    ){
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading
            postRepository.editPost(id,request)
                .onSuccess { posts ->
                    _uiState.value = PostEditUiState.Success(posts)
                }
                .onFailure { error ->
                    _uiState.value = PostEditUiState.Error(error.message ?: "조회 실패")
                }
        }
    }
}