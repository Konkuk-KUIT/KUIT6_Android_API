package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()
    
    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading
            repository.getPostDetail(postId)
                .onSuccess { post ->
                    _uiState.value = PostDetailUiState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostDetailUiState.Error(
                        message = error.message ?: "error"
                    )
                }
        }
    }

    fun deletePost(postId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deletePost(postId)
                .onSuccess {
                    onSuccess()
                }
        }
    }
}

