package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.repository.PostRepository
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse
import com.example.kuit6_android_api.ui.post.state.CreateUiState
import com.example.kuit6_android_api.ui.post.state.PostListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostCreateViewModel(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CreateUiState>(CreateUiState.Idle)
    val uiState: StateFlow<CreateUiState> = _uiState.asStateFlow()

    fun loadCreatePosts(
        author: String,
        title: String,
        content: String,
        imageUrl: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = CreateUiState.Loading

            postRepository.createPost(author,title,content,imageUrl)
                .onSuccess { posts->
                    _uiState.value = CreateUiState.Success(posts)
                }
                .onFailure { error ->
                    _uiState.value = CreateUiState.Error(error.message ?: "error")
                }
        }
    }


}