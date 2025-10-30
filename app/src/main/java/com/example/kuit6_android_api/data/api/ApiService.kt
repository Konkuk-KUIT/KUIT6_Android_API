package com.example.kuit6_android_api.data.api

import com.example.kuit6_android_api.data.model.response.BaseResponse
import com.example.kuit6_android_api.data.model.response.PostResponse
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import retrofit2.http.*

interface ApiService {
    @GET("/api/posts")
    suspend fun getPosts(): BaseResponse<List<PostResponse>>

    @POST("/api/posts")
    suspend fun createPost(
        @Query("author") author: String = "시헌",
        @Body request: PostCreateRequest
    ): BaseResponse<PostResponse>

    @DELETE("/api/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Long
    ): BaseResponse<Unit> // data에 빈 객체 반환

    @GET("/api/posts/{id}")
    suspend fun getPostDetail(
        @Path("id") id: Long
    ): BaseResponse<PostResponse>

    // 업데이트 요청
    @PUT("/api/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Long,
        @Body request: PostCreateRequest // 수정된 내용 전달
    ): BaseResponse<PostResponse>
}