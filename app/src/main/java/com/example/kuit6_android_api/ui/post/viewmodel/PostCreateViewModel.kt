package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.util.UriUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import com.example.kuit6_android_api.ui.post.state.PostCreateUiState

//PostViewModel을 PostListViewModel, PostCreateViewModel, PostDetailViewModel, PostEditViewModel로 분리

class PostCreateViewModel(
    private val repository: PostRepository
    // 의존성 주입:뷰모델의 파라미터로 Repository를 전달하는 것
) : ViewModel() {
    private val _uiState = MutableStateFlow<PostCreateUiState>(PostCreateUiState.Success())
    val uiState: StateFlow<PostCreateUiState> = _uiState.asStateFlow()

    fun createPost(
        author: String,
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = PostCreateUiState.Loading
            repository.createPost(author, title, content, imageUrl)
                .onSuccess {
                    _uiState.value = PostCreateUiState.Success(uploadedImageUrl = null)
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = PostCreateUiState.Error(
                        message = error.message ?: "error"
                    )
                }
        }
    }

    fun clearUploadedImageUrl() {
        val currentState = _uiState.value
        if (currentState is PostCreateUiState.Success) {
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
            if (currentState is PostCreateUiState.Success) {
                _uiState.value = currentState.copy(isUploading = true)
            }
            val file = UriUtils.uriToFile(context, uri)
            if (file == null) {
                val state = _uiState.value
                if (state is PostCreateUiState.Success) {
                    _uiState.value = state.copy(isUploading = false)
                }
                onError("파일 변환 실패")
                return@launch
            }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            repository.uploadImage(body)
                .onSuccess { imageUrl ->
                    _uiState.value = PostCreateUiState.Success(
                        uploadedImageUrl = imageUrl,
                        isUploading = false
                    )
                    onSuccess(imageUrl)
                }
                .onFailure { error ->
                    _uiState.value = PostCreateUiState.Error(
                        message = error.message ?: "업로드 실패"
                    )
                }
        }
    }
}

