package com.example.kuit6_android_api.data.repository

import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse

interface PostRepository {
    suspend fun getPosts(): Result<List<PostResponse>>
    suspend fun getPostDetail(postId: Long): Result<PostResponse>
    suspend fun createPost(author: String, request: PostCreateRequest): Result<PostResponse>
    suspend fun updatePost(postId: Long, request: PostCreateRequest): Result<PostResponse>
    suspend fun deletePost(postId: Long): Result<PostResponse>
}