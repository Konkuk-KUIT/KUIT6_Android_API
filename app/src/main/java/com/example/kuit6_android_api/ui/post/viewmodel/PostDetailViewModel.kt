package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostDetailUIState
import com.example.kuit6_android_api.ui.post.state.PostCreateUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val postRepository: PostRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<PostDetailUIState>(PostDetailUIState.Loading)
    val uiState: StateFlow<PostDetailUIState> = _uiState.asStateFlow()

    fun getPostDetail(postId: Long){
        viewModelScope.launch {
            _uiState.value = PostDetailUIState.Loading

            postRepository.getPostDetail(postId)
                .onSuccess { post ->
                    _uiState.update {
                        it
                    }
                    _uiState.value = PostDetailUIState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostDetailUIState.Error(
                        message = error.message ?: "error"
                    )
                }
        }
    }
}