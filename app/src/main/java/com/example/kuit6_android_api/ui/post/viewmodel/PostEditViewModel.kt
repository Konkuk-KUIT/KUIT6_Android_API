package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.repository.PostRepository
import com.example.kuit6_android_api.ui.post.state.PostEditUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class PostEditViewModel(
    private val postRepository: PostRepository,
    private val postId: Long
) : ViewModel() {
    private val _uiState = MutableStateFlow<PostEditUiState>(PostEditUiState.Loading)
    val uiState: StateFlow<PostEditUiState> = _uiState.asStateFlow()

    private val _uploadedImageUrl = MutableStateFlow<String?>(null)
    val uploadedImageUrl: StateFlow<String?> = _uploadedImageUrl.asStateFlow()

    init {
        getPostDetail()
    }

    fun getPostDetail() {
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading
            postRepository.getPostDetail(postId)
                .onSuccess {
                    _uiState.value = PostEditUiState.Loaded(it)
                }
                .onFailure { error ->
                    _uiState.value = PostEditUiState.Error(
                        message = error.message ?: "getPostDetail error"
                    )
                }
        }
    }

    fun updatePost(
        title: String,
        content: String,
        imageUrl: String?,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _uiState.value = PostEditUiState.Loading
            val request = PostCreateRequest(title, content, imageUrl)
            postRepository.updatePost(postId, request)
                .onSuccess { response ->
                    _uiState.value = PostEditUiState.Success(response)
                    //clearUploadedImageUrl()
                    onSuccess()
                }.onFailure { error ->
                    _uiState.value = PostEditUiState.Error(
                        message = error.message ?: "updatePost error"
                    )
                }
        }
    }

    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            val file = uriToFile(context, uri)
            if (file == null) {
                _uiState.value = PostEditUiState.Error("파일 변환 실패")
                onError("파일 변환 실패")
                return@launch
            }
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            postRepository.uploadImage(body)
                .onSuccess { map ->
                    val imageUrl = map["imageUrl"]
                    if (!imageUrl.isNullOrBlank()) {
                        _uploadedImageUrl.value = imageUrl
                    } else {
                        _uiState.value = PostEditUiState.Error("이미지 URL이 없습니다.")
                        onError("업로드 실패")
                    }
                }.onFailure { error ->
                    _uiState.value = PostEditUiState.Error(
                        error.message ?: "Image 업로드 실패"
                    )
                    onError(error.message ?: "Image 업로드 실패")
                }
        }
    }

    fun clearUploadedImageUrl() {
        _uploadedImageUrl.value = null
    }

    // URI를 File로 변환하는 헬퍼 함수
    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val fileName = getFileName(context, uri) ?: "image_${System.currentTimeMillis()}.jpg"
            val tempFile = File(context.cacheDir, fileName)

            contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }
}