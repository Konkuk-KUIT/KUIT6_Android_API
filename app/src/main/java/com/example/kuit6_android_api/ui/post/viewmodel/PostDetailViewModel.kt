package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.lifecycle.SavedStateHandle
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
    private val postRepository: PostRepository,
    savedStateHandle: SavedStateHandle
//    private val postId: Long
) : ViewModel() {
    private val postId: Long =
        checkNotNull(savedStateHandle.get<Long>("postId"))
    private val _uiState = MutableStateFlow<PostDetailUiState>(PostDetailUiState.Loading)
    val uiState: StateFlow<PostDetailUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading
            postRepository.getPostDetail(postId)
                .onSuccess { post ->
                    _uiState.value = PostDetailUiState.Success(post)
                }
                .onFailure { error ->
                    _uiState.value = PostDetailUiState.Error(
                        message = error.message ?: "PostDetailViewModel refresh error"
                    )
                }
        }
    }

    fun deletePost(onSuccess: () -> Unit = {},
                   onFailure: (String) -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = PostDetailUiState.Loading
            postRepository.deletePost(postId)
                .onSuccess { onSuccess() }
                .onFailure { error ->
                    _uiState.value = PostDetailUiState.Error(
                        message = error.message ?: "error"
                    )
                    onFailure(error.message ?: "error")
                }
        }
    }
}