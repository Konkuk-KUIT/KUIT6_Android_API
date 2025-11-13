package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PostViewModel : ViewModel() {
    var posts by mutableStateOf<List<PostResponse>>(emptyList())
        private set

    var postDetail by mutableStateOf<PostResponse?>(null)
        private set

    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

    private val apiService = RetrofitClient.apiService

    fun getPostDetail(postId: Long) {
        viewModelScope.launch {
            runCatching {
                apiService.getPostDetail(postId)
            }.onSuccess { response ->
                response.data?.let{
                    if(response.success)
                        postDetail = response.data
                }
            }
        }
    }

    fun createPost(
        author: String,
        title: String,
        content: String,
        imageUrl: String? = null,
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
        imageUrl: String? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            runCatching {
                val request = PostCreateRequest(title,content,imageUrl)
                apiService.updatePost(postId, request)
            }.onSuccess { response ->
                if(response.success && response.data != null){
                    posts = posts.map{
                        if(it.id == postId) response.data else it
                    }
                    postDetail = response.data // postDetail을 갱신된 객체로 바꾸기
                    onSuccess()
                }
            }
        }
    }

    fun deletePost(postId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
                runCatching {
                    apiService.deletePost(postId)
                }.onSuccess { response ->
                    if (response.success) {
                        posts = posts.filterNot { it.id == postId }
                        onSuccess()
                    }
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
