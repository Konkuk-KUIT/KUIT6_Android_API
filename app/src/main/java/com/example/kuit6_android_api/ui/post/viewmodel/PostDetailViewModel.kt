package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.CreateUiState
import com.example.kuit6_android_api.ui.post.state.PostDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val repository: PostRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    fun loadPost(id: Long) {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading
            repository.getPostDetail(id)
                .onSuccess { posts ->
                    _uiState.value = PostDetailUiState.Success(posts)
                }
                .onFailure { error ->
                    _uiState.value = PostDetailUiState.Error(error.message ?: "조회 실패")
                }
        }
    }

    fun deletePost(id: Long) {
        viewModelScope.launch {
            repository.deletePost(id)
                .onSuccess {
                    _uiState.value = PostDetailUiState.Deleted()
                }
                .onFailure { error ->
                    _uiState.value = PostDetailUiState.Error(error.message ?: "삭제 실패")
                }
        }
    }

}