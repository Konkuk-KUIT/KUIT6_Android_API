package com.example.kuit6_android_api.data.repository

import android.util.Log
import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse

class PostRepositoryImpl(
    private val apiService: ApiService
): PostRepository{
    override suspend fun getPosts(): Result<List<PostResponse>> {
        return runCatching {
            val response = apiService.getPosts()

            if (response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?: "게시글 불러오기 실패")
            }
        }.onFailure { error->
            Log.e("PostRepository",error.message.toString())
        }
    }

    override suspend fun getPostDetail(postId: Long): Result<PostResponse> {
        return runCatching {
            val response = apiService.getPostsDetail(postId)

            if (response.success && response.data != null){
                response.data
            } else{
                throw Exception(response.message ?: "삭제 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun createPost(
        author: String,
        request: PostCreateRequest
    ): Result<PostResponse> {
        return runCatching {
            val response = apiService.createPost(author, request)
            if (response.success && response.data != null){
                response.data
            } else{
                throw Exception(response.message ?: "생성 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun updatePost(
        postId: Long,
        request: PostCreateRequest
    ): Result<PostResponse> {
        return runCatching {
            val response = apiService.updatePost(postId, request)
            if (response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?: "생성 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun deletePost(postId: Long): Result<PostResponse> {
        return runCatching {
            val response = apiService.deletePost(postId)
            if (response.success && response.data != null){
                response.data
            }else{
                throw Exception(response.message ?: "생성 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }
}