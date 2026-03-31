package com.example.kuit6_android_api.data.repository

import android.util.Log
import com.example.kuit6_android_api.data.api.ApiService
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.BaseResponse
import com.example.kuit6_android_api.data.model.response.PostResponse
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl
    @Inject
    constructor(
        private val apiService: ApiService,
    ) : PostRepository {
        override suspend fun getPosts(): Result<List<PostResponse>> =
            runCatching {
                val response: BaseResponse<List<PostResponse>> = apiService.getPosts()
                if (response.success && response.data != null) {
                    response.data
                } else {
                    throw Exception(response.message ?: "게시글 목록 조회 실패")
                }
            }.onFailure { error ->
                Log.e("PostRepository", error.message.toString())
            }

        override suspend fun getPostDetail(postId: Long): Result<PostResponse> =
            runCatching {
                val response: BaseResponse<PostResponse> = apiService.getPostDetail(postId)
                if (response.success && response.data != null) {
                    response.data
                } else {
                    throw Exception(response.message ?: "게시글 상세 조회 실패")
                }
            }

        override suspend fun createPost(
            author: String,
            title: String,
            content: String,
            imageUrl: String?,
        ): Result<PostResponse> =
            runCatching {
                val request = PostCreateRequest(title, content, imageUrl)
                val response: BaseResponse<PostResponse> = apiService.createPost(author, request)
                if (response.success && response.data != null) {
                    response.data
                } else {
                    throw Exception(response.message ?: "게시글 생성 실패")
                }
            }

        override suspend fun updatePost(
            postId: Long,
            title: String,
            content: String,
            imageUrl: String?,
        ): Result<PostResponse> =
            runCatching {
                val request = PostCreateRequest(title, content, imageUrl)
                val response: BaseResponse<PostResponse> = apiService.updatePost(postId, request)
                if (response.success && response.data != null) {
                    response.data
                } else {
                    throw Exception(response.message ?: "게시글 수정 실패")
                }
            }

        override suspend fun deletePost(postId: Long): Result<Unit> =
            runCatching {
                val response: BaseResponse<Unit> = apiService.deletePost(postId)
                if (response.success) {
                    Unit
                } else {
                    throw Exception(response.message ?: "게시글 삭제 실패")
                }
            }

        override suspend fun uploadImage(file: MultipartBody.Part): Result<String> =
            runCatching {
                val response: BaseResponse<Map<String, String>> = apiService.uploadImage(file)
                if (response.success && response.data != null) {
                    val imageUrl = response.data["imageUrl"]
                    if (imageUrl != null) {
                        imageUrl
                    } else {
                        throw Exception("이미지 URL을 받아오지 못했습니다")
                    }
                } else {
                    throw Exception(response.message ?: "이미지 업로드 실패")
                }
            }
    }
