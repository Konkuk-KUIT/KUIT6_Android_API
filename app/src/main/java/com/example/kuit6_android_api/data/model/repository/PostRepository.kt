package com.example.kuit6_android_api.data.model.repository

import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse

interface  PostRepository {
    suspend fun getPost(): Result<List<PostResponse>>
    suspend fun createPost(
        author: String,
        title: String,
        content: String,
        imageUrl: String? = null
    ): Result<PostResponse>

    suspend fun getPostDetail(
        id: Long
    ): Result<PostResponse>

    suspend fun deletePost(
        id: Long
    ): Result<PostResponse>

    suspend fun editPost(
        id: Long,
        request: PostCreateRequest
    ): Result<PostResponse>
}