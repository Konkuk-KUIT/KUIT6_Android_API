package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostListUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostListViewModel(
    private val postRepository: PostRepository
): ViewModel(){
    private val _uiState = MutableStateFlow<PostListUIState>(PostListUIState.Loading)
    val uiState: StateFlow<PostListUIState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts(){
        viewModelScope.launch {
            _uiState.value = PostListUIState.Loading

            postRepository.getPosts()
                .onSuccess { posts->
                    _uiState.value = PostListUIState.Success(posts)
                }
                .onFailure { error->
                    _uiState.value = PostListUIState.Error(
                        message = error.message ?: "error"
                    )
                }
        }
    }

    fun refresh(){
        loadPosts()
    }
}