package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostEditUiState
import com.example.kuit6_android_api.util.UriUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PostEditUiState>(PostEditUiState.Loading)
    val uiState: StateFlow<PostEditUiState> = _uiState.asStateFlow()

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading
            repository.getPostDetail(postId)
                .onSuccess { post ->
                    _uiState.value = PostEditUiState.Success(postDetail = post)
                }
                .onFailure { error ->
                    _uiState.value = PostEditUiState.Error(
                        message = error.message ?: "error"
                    )
                }
        }
    }

    fun updatePost(
        postId: Long,
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading
            repository.updatePost(postId, title, content, imageUrl)
                .onSuccess {
                    val currentState = _uiState.value
                    if (currentState is PostEditUiState.Success) {
                        _uiState.value = currentState.copy(uploadedImageUrl = null)
                    }
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = PostEditUiState.Error(
                        message = error.message ?: "error"
                    )
                }
        }
    }

    fun clearUploadedImageUrl() {
        val currentState = _uiState.value
        if (currentState is PostEditUiState.Success) {
            _uiState.value = currentState.copy(uploadedImageUrl = null)
        }
    }

    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is PostEditUiState.Success) {
                _uiState.value = currentState.copy(isUploading = true)
            }
            val file = UriUtils.uriToFile(context, uri)
            if (file == null) {
                val state = _uiState.value
                if (state is PostEditUiState.Success) {
                    _uiState.value = state.copy(isUploading = false)
                }
                onError("파일 변환 실패")
                return@launch
            }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            repository.uploadImage(body)
                .onSuccess { imageUrl ->
                    val state = _uiState.value
                    if (state is PostEditUiState.Success) {
                        _uiState.value = state.copy(
                            uploadedImageUrl = imageUrl,
                            isUploading = false
                        )
                    }
                    onSuccess(imageUrl)
                }
                .onFailure { error ->
                    _uiState.value = PostEditUiState.Error(
                        message = error.message ?: "업로드 실패"
                    )
                }
        }
    }
}

