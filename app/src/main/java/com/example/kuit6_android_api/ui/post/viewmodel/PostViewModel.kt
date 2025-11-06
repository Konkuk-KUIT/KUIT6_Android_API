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
import kotlinx.serialization.builtins.serializer


class PostViewModel : ViewModel() {

    var posts by mutableStateOf<List<PostResponse>>(emptyList())
        private set

    var postDetail by mutableStateOf<PostResponse?>(null)
        private set

    private val apiService = RetrofitClient.apiService
    var uploadedImageUrl by mutableStateOf<String?>(null)
        private set

    /*
    //얜 이제 사용을 안함
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
    */


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
            //예외처리
            runCatching {
                //서버에 보낼 데이터를 PostCreateRequest에 갑싸서 준비
                val request = PostCreateRequest(title, content, imageUrl)
                apiService.createPost(author, request)

            }.onSuccess { response ->//성공 처리
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
                val request = PostCreateRequest(title, content, imageUrl)
                apiService.editPost(postId, request)
            }.onSuccess { response ->
                if(response.success){
                    postDetail = response.data
                    onSuccess()
                }
            }
        }
    }

    fun deletePost(
        postId: Long,
        onSuccess: () -> Unit = {}
    ) {
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
