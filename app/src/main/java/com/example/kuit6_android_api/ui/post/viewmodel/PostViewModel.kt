package com.example.kuit6_android_api.ui.post.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.kuit6_android_api.data.api.RetrofitClient
import com.example.kuit6_android_api.data.model.response.PostResponse
import com.example.kuit6_android_api.data.model.request.PostCreateRequest


class PostViewModel : ViewModel() {

    var posts by mutableStateOf<List<PostResponse>>(emptyList())
        private set

    var postDetail by mutableStateOf<PostResponse?>(null)
        private set

    private val apiService = RetrofitClient.apiService
    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

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
                apiService.getPostDetail(postId)
            }.onSuccess { response ->
                response.data?.let{
                    if(response.success){
                        postDetail = response.data
                    }
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
                apiService.editPost(postId, PostCreateRequest(title, content, imageUrl))
                }.onSuccess{ response->
                    if(response.success){
                        postDetail = response.data
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
                if(response.success){
                onSuccess()
                }
            }
        }
    }

    fun clearUploadedImageUrl() {
        uploadedImageUrl = null
    }

}
