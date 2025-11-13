package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostCreateViewModel(
    private val postRepository: PostRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<PostUIState>(PostUIState.Loading)
    val uiState: StateFlow<PostUIState> = _uiState.asStateFlow()

    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

    fun createPost(author: String = "anonymous", title: String, content: String, imageUrl: String? = null){
        viewModelScope.launch {
            _uiState.value = PostUIState.Loading
            val request = PostCreateRequest(title, content, imageUrl)
            postRepository.createPost(author, request)
                .onSuccess { post ->
                    _uiState.value = PostUIState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostUIState.Error(
                        message = error.message ?: "error"
                    )
                }

            clearUploadedImageUrl()
        }
    }

    fun clearUploadedImageUrl(){
        uploadedImageUrl = null
    }
}