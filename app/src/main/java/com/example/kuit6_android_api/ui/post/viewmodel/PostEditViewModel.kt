package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.data.model.response.PostResponse
import com.example.kuit6_android_api.util.UriUtils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

data class PostEditUiState(
    val postDetail: PostResponse? = null,
    val uploadedImageUrl: String? = null,
    val isUploading: Boolean = false
)

class PostEditViewModel(
    private val repository: PostRepository
) : ViewModel() {
    var uiState by mutableStateOf(PostEditUiState())
        private set

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            repository.getPostDetail(postId)
                .onSuccess { post ->
                    uiState = uiState.copy(postDetail = post)
                }
                .onFailure {
                    uiState = uiState.copy(postDetail = null)
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
            repository.updatePost(postId, title, content, imageUrl)
                .onSuccess {
                    uiState = uiState.copy(uploadedImageUrl = null)
                    onSuccess()
                }
        }
    }

    fun clearUploadedImageUrl() {
        uiState = uiState.copy(uploadedImageUrl = null)
    }

    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            uiState = uiState.copy(isUploading = true)
            val file = UriUtils.uriToFile(context, uri)
            if (file == null) {
                uiState = uiState.copy(isUploading = false)
                onError("파일 변환 실패")
                return@launch
            }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            repository.uploadImage(body)
                .onSuccess { imageUrl ->
                    uiState = uiState.copy(
                        isUploading = false,
                        uploadedImageUrl = imageUrl
                    )
                    onSuccess(imageUrl)
                }
                .onFailure { error ->
                    uiState = uiState.copy(isUploading = false)
                    onError(error.message ?: "업로드 실패")
                }
        }
    }
}

