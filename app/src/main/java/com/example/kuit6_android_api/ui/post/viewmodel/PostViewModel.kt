package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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

    private var nextId = 4L

    var posts by mutableStateOf<List<PostResponse>>(emptyList())
        private set

    var postDetail by mutableStateOf<PostResponse?>(null)
        private set

    var uploadedImageUrl by mutableStateOf<String?>(null)
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

        }
    }

    fun createPost(
        author: String = "anonymous",
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
                clearUploadedImageUrl()
                onSuccess()
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

        }
    }

    fun deletePost(postId: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {

        }
    }

    fun clearUploadedImageUrl() {
        uploadedImageUrl = null
    }

}

