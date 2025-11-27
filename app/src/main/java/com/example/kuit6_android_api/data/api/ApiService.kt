package com.example.kuit6_android_api.data.api

import com.example.kuit6_android_api.data.model.request.LoginRequest
import com.example.kuit6_android_api.data.model.response.BaseResponse
import com.example.kuit6_android_api.data.model.response.PostResponse
import com.example.kuit6_android_api.data.model.request.PostCreateRequest
import com.example.kuit6_android_api.data.model.response.LoginResponse
import okhttp3.MultipartBody
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
    ): BaseResponse<PostResponse> // data에 빈 객체 반환

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

    @Multipart
    @POST("/api/images/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): BaseResponse<Map<String, String>>

    @POST("/api/auth/signup")
    suspend fun signup(
        @Body request: LoginRequest
    ): BaseResponse<LoginResponse>

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): BaseResponse<LoginResponse>

    @GET("/api/auth/validate")
    suspend fun validateToken(): BaseResponse<Boolean>
}