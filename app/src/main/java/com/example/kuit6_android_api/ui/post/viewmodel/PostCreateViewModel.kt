package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.util.UriUtils
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

//PostViewModel을 PostListViewModel, PostCreateViewModel, PostDetailViewModel, PostEditViewModel로 분리
data class PostCreateUiState( //uiState를 통해 상태를 한 번에 모아 처리
    val uploadedImageUrl: String? = null,
    val isUploading: Boolean = false
)

class PostCreateViewModel(
    private val repository: PostRepository
    // 의존성 주입:뷰모델의 파라미터로 Repository를 전달하는 것
) : ViewModel() {
    //st
    var uiState by mutableStateOf(PostCreateUiState())
        private set

    fun createPost(
        author: String,
        title: String,
        content: String,
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            repository.createPost(author, title, content, imageUrl)
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

