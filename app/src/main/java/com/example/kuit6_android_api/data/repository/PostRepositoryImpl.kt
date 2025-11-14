package com.example.kuit6_android_api.data.repository

import android.util.Log
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.PostResponse
import com.example.kuit6_android_api.data.service.ApiService
import okhttp3.MultipartBody

class PostRepositoryImpl(
    private val apiService: ApiService
) : PostRepository {
    override suspend fun getPosts(): Result<List<PostResponse>> {
        return runCatching {
            val response = apiService.getPosts()

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "게시글 불러오기 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }

    // TODO: 나머지 함수 구현
    override suspend fun getPostDetail(postId: Long): Result<PostResponse> {
        return runCatching {
            val response = apiService.getDetail(postId)

            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "게시글 상세 불러오기 실패")
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
            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "게시글 생성 실패")
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
            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "게시글 수정 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun deletePost(postId: Long): Result<Unit> {
        return runCatching {
            val response = apiService.deletePost(postId)
            if (response.success) {
                Unit
            } else {
                throw Exception(response.message ?: "게시글 삭제 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }

    override suspend fun uploadImage(filePart: MultipartBody.Part): Result<Map<String, String>> {
        return runCatching {
            val response = apiService.uploadImage(filePart)
            if (response.success && response.data != null) {
                response.data
            } else {
                throw Exception(response.message ?: "이미지 업로드 실패")
            }
        }.onFailure { error ->
            Log.e("PostRepository", error.message.toString())
        }
    }
}