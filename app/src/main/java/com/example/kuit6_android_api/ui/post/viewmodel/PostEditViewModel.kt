package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostEditViewModel (
    private val postRepository: PostRepository
): ViewModel(){
    private val _uiState = MutableStateFlow<PostUIState>(PostUIState.Loading)
    val uiState: StateFlow<PostUIState> = _uiState.asStateFlow()


    fun deletePost(postId: Long){
        viewModelScope.launch {
            _uiState.value = PostUIState.Loading

            postRepository.deletePost(postId)
                .onSuccess { post ->
                    _uiState.value = PostUIState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostUIState.Error(
                        message = error.message ?: "error"
                    )
                }
        }
    }

    fun updatePost(postId: Long, title: String, content: String, imageUrl: String? = null){
        viewModelScope.launch {
            _uiState.value = PostUIState.Loading
            val request = PostCreateRequest(title, content, imageUrl)

            postRepository.updatePost(postId, request)
                .onSuccess { post ->
                    _uiState.value = PostUIState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostUIState.Error(
                        message = error.message ?: "error"
                    )
                }
        }
    }
}