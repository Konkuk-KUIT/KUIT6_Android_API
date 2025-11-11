package com.example.kuit6_android_api.ui.post.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

class PostViewModel : ViewModel() {
    var posts by mutableStateOf<List<PostResponse>>(emptyList())
        private set

    var postDetail by mutableStateOf<PostResponse?>(null)
        private set

    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

    var isUploading by mutableStateOf(false)
        private set

    private val apiService = RetrofitClient.apiService
    fun getPosts() {
        viewModelScope.launch {
            runCatching {
                apiService.getPosts()
            }.onSuccess { response ->
                response.data?.let {
                    if (response.success) {
                        posts = response.data
                    }
                }
            }
        }
    }

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            runCatching {
                apiService.getDetail(postId)
            }.onSuccess { response ->
                response.data?.let {
                    if (response.success) {
                        postDetail = response.data
                    }
                }
            }
        }
    }

    fun createPost(
        author: String,// = "anonymous",
        title: String,
        content: String,
        imageUrl: String? = uploadedImageUrl,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                val request = PostCreateRequest(title, content, imageUrl)
                apiService.createPost(author, request)
            }.onSuccess { response ->
                if (response.success) {
                    // 이미지 업로드 관련 코드
                    clearUploadedImageUrl()
                    onSuccess()
                }
            }
        }
    }

    fun updatePost(
        postId: Long,
        title: String,
        content: String,
        imageUrl: String?,
        onSuccess: () -> Unit = {}
    ) {
        // 수정 후 게시글 상세가 나오게
        viewModelScope.launch {
            runCatching {
                val request = PostCreateRequest(title, content, imageUrl)
                apiService.updatePost(postId, request)
            }.onSuccess { response ->
                if (response.success && response.data != null) {
                    postDetail = response.data
                    onSuccess()
                }
            }
        }
    }

    fun deletePost(postId: Long, onSuccess: () -> Unit = {}) {
        // 삭제 후 게시글 목록 나오게
        viewModelScope.launch {
            runCatching {
                apiService.deletePost(postId)
            }.onSuccess { response ->
                if (response.success) {
                    postDetail = null
                    getPosts()
                    onSuccess()
                }
            }
        }
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

    // 이미지 업로드 함수
    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            isUploading = true
            runCatching {
                val file = uriToFile(context, uri)
                if (file == null) {
                    throw Exception("파일 변환 실패")
                }

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                apiService.uploadImage(body)
            }.onSuccess { response ->
                isUploading = false // 로딩 종료
                if (response.success && response.data != null) {
                    val imageUrl = response.data["imageUrl"]
                    if (imageUrl != null) {
                        uploadedImageUrl = imageUrl
                        onSuccess(imageUrl)
                    }
                }
            }.onFailure { error ->
                isUploading = false
                onError(error.message ?: "업로드 실패")
            }
        }
    }

    fun clearUploadedImageUrl() {
        uploadedImageUrl = null
    }

    private fun getCurrentDateTime(): String {
        return LocalDateTime.now().toString()
    }
}
