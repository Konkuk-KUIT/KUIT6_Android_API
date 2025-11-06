package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.startup.Initializer
import com.example.kuit6_android_api.App
import com.example.kuit6_android_api.data.model.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostListViewModel (
    private val postRepository : PostRepository
) : ViewModel(){
    //값 변경하려고 사용함
    private val _uiState = MutableStateFlow<PostListUiState>(PostListUiState.Loading)
    val uiState: StateFlow<PostListUiState> = _uiState.asStateFlow()

    init{
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = PostListUiState.Loading

            postRepository.getPost()
                .onSuccess { posts->
                    _uiState.value = PostListUiState.Success(posts)
                }
                .onFailure { error->
                    _uiState.value = PostListUiState.Error(
                        message = error.message ?: "error"
                    )
                }
        }
    }

    fun refresh(){
        loadPosts()
    }
}

