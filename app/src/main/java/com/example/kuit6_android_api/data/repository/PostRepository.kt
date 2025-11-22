package com.example.kuit6_android_api.data.repository

import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse
import okhttp3.MultipartBody

interface PostRepository {
    suspend fun getPosts(): Result<List<PostResponse>>
    //레포지토리 패턴을 사용하기 위해 다음 함수들을 추가
    suspend fun getPostDetail(postId: Long): Result<PostResponse>
    suspend fun createPost(
        author: String,
        title: String,
        content: String,
        imageUrl: String?
    ): Result<PostResponse>
    suspend fun updatePost(
        postId: Long,
        title: String,
        content: String,
        imageUrl: String?
    ): Result<PostResponse>
    suspend fun deletePost(postId: Long): Result<Unit>
    suspend fun uploadImage(file: MultipartBody.Part): Result<String>
}
